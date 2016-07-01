import java.util.LinkedList;
import java.util.List;

import org.opencv.calib3d.Calib3d;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfDMatch;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.DMatch;
import org.opencv.features2d.DescriptorExtractor;
import org.opencv.features2d.DescriptorMatcher;
import org.opencv.features2d.FeatureDetector;
import org.opencv.core.KeyPoint;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import static org.opencv.core.CvType.CV_32F;

public class Processor {
    private String file;
    private MatOfKeyPoint factureDescriptors;
    private MatOfKeyPoint factureKeyPoints;
    private FeatureDetector featureDetector;
    private DescriptorExtractor descriptorExtractor;

    private void init() {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        Mat facture = Imgcodecs.imread(file, Imgcodecs.CV_LOAD_IMAGE_COLOR);
        featureDetector = FeatureDetector.create(FeatureDetector.DYNAMIC_BRISK);
        descriptorExtractor = DescriptorExtractor.create(DescriptorExtractor.BRISK);
        System.out.println("Initialisation...");

        factureKeyPoints = new MatOfKeyPoint();
        factureDescriptors = new MatOfKeyPoint();

        featureDetector.detect(facture, factureKeyPoints);
        descriptorExtractor.compute(facture, factureKeyPoints, factureDescriptors);
    }

    private List<Point> getBorder(Mat bloc, MatOfKeyPoint blocKeyPoints, LinkedList<DMatch> goodMatchesList) {
        List<KeyPoint> blocKeyPointList = blocKeyPoints.toList();
        List<KeyPoint> factureKeyPointList = factureKeyPoints.toList();

        LinkedList<Point> blocPoints = new LinkedList<>();
        LinkedList<Point> facturePoints = new LinkedList<>();

        for (int i = 0; i < goodMatchesList.size(); i++)
        {
            blocPoints.addLast(blocKeyPointList.get(goodMatchesList.get(i).queryIdx).pt);
            facturePoints.addLast(factureKeyPointList.get(goodMatchesList.get(i).trainIdx).pt);
        }

        MatOfPoint2f blocMatOfPoint2f = new MatOfPoint2f();
        blocMatOfPoint2f.fromList(blocPoints);
        MatOfPoint2f factureMatOfPoint2f = new MatOfPoint2f();
        factureMatOfPoint2f.fromList(facturePoints);

        Mat homography = Calib3d.findHomography(blocMatOfPoint2f, factureMatOfPoint2f, Calib3d.RANSAC, 3);

        Mat blocCorners = new Mat(4, 1, CvType.CV_32FC2);
        Mat factureCorners = new Mat(4, 1, CvType.CV_32FC2);

        blocCorners.put(0, 0, new double[] { 0, 0 });
        blocCorners.put(1, 0, new double[] { bloc.cols(), 0 });
        blocCorners.put(2, 0, new double[] { bloc.cols(), bloc.rows() });
        blocCorners.put(3, 0, new double[] { 0, bloc.rows() });

        Core.perspectiveTransform(blocCorners, factureCorners, homography);

        List<Point> res = new LinkedList<>();

        res.add(new Point(factureCorners.get(0, 0)));
        res.add(new Point(factureCorners.get(1, 0)));
        res.add(new Point(factureCorners.get(2, 0)));
        res.add(new Point(factureCorners.get(3, 0)));
        return (res);
    }

    private List<Point> processOne(int index) {
        System.out.println("Recherche du bloc " + index + "...");
        Mat bloc = Imgcodecs.imread("blocs//bloc" + index + ".jpg", Imgcodecs.CV_LOAD_IMAGE_COLOR);

        MatOfKeyPoint blocKeyPoints = new MatOfKeyPoint();
        MatOfKeyPoint blocDescriptors = new MatOfKeyPoint();

        featureDetector.detect(bloc, blocKeyPoints);
        descriptorExtractor.compute(bloc, blocKeyPoints, blocDescriptors);

        List<MatOfDMatch> matches = new LinkedList<>();

        DescriptorMatcher descriptorMatcher = DescriptorMatcher.create(DescriptorMatcher.BRUTEFORCE_L1);
        if (blocDescriptors.type() != CV_32F) {
            blocDescriptors.convertTo(blocDescriptors, CV_32F);
        }
        if (factureDescriptors.type() != CV_32F) {
            factureDescriptors.convertTo(factureDescriptors, CV_32F);
        }
        descriptorMatcher.knnMatch(blocDescriptors, factureDescriptors, matches, 2);

        LinkedList<DMatch> goodMatchesList = new LinkedList<>();
        float nndrRatio = 0.7f;

        for (int i = 0; i < matches.size(); i++) {
            MatOfDMatch matofDMatch = matches.get(i);
            DMatch[] dMatchArray = matofDMatch.toArray();
            DMatch m1 = dMatchArray[0];
            DMatch m2 = dMatchArray[1];

            if (m1.distance <= m2.distance * nndrRatio) {
                goodMatchesList.addLast(m1);
            }
        }
        System.out.println(goodMatchesList.size());
        if (goodMatchesList.size() > 15) {
            System.out.println("Bloc trouvé !");
            return (getBorder(bloc, blocKeyPoints, goodMatchesList));
        }
        System.out.println("Bloc pas trouvé...");
        return (new LinkedList<>());
    }

    public List<List<List<Double>>> run(String str) {
        file = str;
        init();
        List<List<Point>> list = new LinkedList<>();
        List<List<List<Double>>> res = new LinkedList<>();

        for (int i = 1; i <= 13; i++) {
            list.add(processOne(i));
        }

        int recto = 0;
        int verso = 0;

        for (int i = 0; i < list.size(); i++) {
            if (i < 9 && list.get(i).size() > 0) {
                recto++;
            } else if (i >= 9 && list.get(i).size() > 0) {
                verso++;
            }
        }

        boolean firstPage = recto > verso;
        if ((firstPage && recto < 3) || (!firstPage && verso < 3)) {
            System.out.println("Aucune page.......");
            return res;
        }

        System.out.println((firstPage ? "Première" : "Deuxième") + " page !");


        Mat img = Imgcodecs.imread(file, Imgcodecs.CV_LOAD_IMAGE_COLOR);
        for (int i = 0; i < list.size(); i++) {
            List<Point> bloc = list.get(i);
            if (bloc.size() > 0 && ((firstPage && i < 9) || (!firstPage && i >= 9))) {
                List<List<Double>> toAdd = new LinkedList<>();
                List<Double> pt = new LinkedList<>();
                pt.add(bloc.get(0).x);
                pt.add(bloc.get(0).y);
                toAdd.add(pt);
                pt = new LinkedList<>();
                pt.add(bloc.get(2).x);
                pt.add(bloc.get(2).y);
                toAdd.add(pt);
                pt = new LinkedList<>();
                pt.add((double) i);
                toAdd.add(pt);
                res.add(toAdd);
                /*
                Imgproc.line(img, bloc.get(0), bloc.get(1), new Scalar(0, 255, 0), 4);
                Imgproc.line(img, bloc.get(1), bloc.get(2), new Scalar(0, 255, 0), 4);
                Imgproc.line(img, bloc.get(2), bloc.get(3), new Scalar(0, 255, 0), 4);
                Imgproc.line(img, bloc.get(3), bloc.get(0), new Scalar(0, 255, 0), 4);
                */
            }
        }
        //Imgcodecs.imwrite("img.jpg", img);
        return res;
    }
}
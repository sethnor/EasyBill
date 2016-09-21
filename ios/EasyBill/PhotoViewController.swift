//
//  PhotoViewController.swift
//  EasyBill
//
//  Created by Jacques LORENTZ on 29/06/2016.
//  Copyright © 2016 Jacques LORENTZ. All rights reserved.
//

import UIKit

class PhotoViewController: UIViewController, UIImagePickerControllerDelegate, UINavigationControllerDelegate {

    @IBOutlet weak var help: UITextView!
    @IBOutlet weak var spinner: UIActivityIndicatorView!
    @IBOutlet weak var photoButton: UIButton!
    @IBOutlet weak var img: UIImageView!
    @IBAction func button(_ sender: UIButton) {
        let imagePicker = UIImagePickerController()
        
        imagePicker.delegate = self
        imagePicker.sourceType =
            UIImagePickerControllerSourceType.camera
        imagePicker.allowsEditing = false
        self.present(imagePicker, animated: true,
                                   completion: nil)
    }
    var image: UIImage = UIImage()

    func imagePickerController(_ picker: UIImagePickerController, didFinishPickingImage imageTmp: UIImage, editingInfo: [String : AnyObject]?) {
        
        help.isHidden = true
        spinner.isHidden = false
        spinner.startAnimating()
        photoButton.isHidden = true
        image = imageTmp
        self.dismiss(animated: true, completion: receiveCoord)
    }
    
    func receiveCoord()  {
        let ret: Data = conn.sendImg(image)
        
        do {
            let result: AnyObject = try JSONSerialization.jsonObject(with: ret, options: JSONSerialization.ReadingOptions()) as AnyObject
            for i in result as! [[[Double]]] {
                
                let scale: Double = 800 / Double(image.size.width)
                
                let x1: Double = (i[0][0] / (Double(image.size.width) * scale)) * 300 + 15
                let y1: Double = (i[0][1] / (Double(image.size.height) * scale)) * 400 + 149
                let x2: Double = (i[1][0] / (Double(image.size.width) * scale)) * 300 + 15
                let y2: Double = (i[1][1] / (Double(image.size.height) * scale)) * 400 + 149
                
                let button: UIButton = UIButton(frame: CGRect(x: CGFloat(x1), y: CGFloat(y1), width: CGFloat(x2 - x1), height: CGFloat(y2 - y1)))
                button.backgroundColor = UIColor.clear
                button.layer.borderWidth = 1
                button.layer.borderColor = UIColor.green.cgColor
                button.setTitle("", for: UIControlState())
                button.addTarget(self, action: #selector(PhotoViewController.buttonAction(_:)), for: UIControlEvents.touchUpInside)
                button.tag = Int(i[2][0]) * 1000 + Int(i[3][0])
                self.view.addSubview(button)
            }
        } catch {
            print(error)
        }
        
        spinner.isHidden = true
        img.image = image
        img.isHidden = false

    }
    
    func buttonAction(_ sender: UIButton!) {
        let button: UIButton = sender
        questionsMode = button.tag % 1000
        questionPage = button.tag / 1000
        let vc = self.storyboard?.instantiateViewController(withIdentifier: "questionsView")
        self.show(vc!, sender: vc)
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        spinner.isHidden = true
        img.isHidden = true
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
    }
}

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
    @IBAction func button(sender: UIButton) {
        let imagePicker = UIImagePickerController()
        
        imagePicker.delegate = self
        imagePicker.sourceType =
            UIImagePickerControllerSourceType.Camera
        imagePicker.allowsEditing = false
        self.presentViewController(imagePicker, animated: true,
                                   completion: nil)
    }
    var image: UIImage = UIImage()

    func imagePickerController(picker: UIImagePickerController, didFinishPickingImage imageTmp: UIImage, editingInfo: [String : AnyObject]?) {
        
        help.hidden = true
        spinner.hidden = false
        spinner.startAnimating()
        photoButton.hidden = true
        image = imageTmp
        self.dismissViewControllerAnimated(true, completion: receiveCoord)
    }
    
    func receiveCoord()  {
        var result:AnyObject = conn.sendImg(image)
        
        do {
            result = try NSJSONSerialization.JSONObjectWithData(result as! NSData, options: NSJSONReadingOptions())
            for i in result as! [[[Double]]] {
                
                let scale: Double = 800 / Double(image.size.width)
                
                let x1: Double = (i[0][0] / (Double(image.size.width) * scale)) * 300 + 15
                let y1: Double = (i[0][1] / (Double(image.size.height) * scale)) * 400 + 149
                let x2: Double = (i[1][0] / (Double(image.size.width) * scale)) * 300 + 15
                let y2: Double = (i[1][1] / (Double(image.size.height) * scale)) * 400 + 149
                
                let button: UIButton = UIButton(frame: CGRectMake(CGFloat(x1), CGFloat(y1), CGFloat(x2 - x1), CGFloat(y2 - y1)))
                button.backgroundColor = UIColor.clearColor()
                button.layer.borderWidth = 1
                button.layer.borderColor = UIColor.greenColor().CGColor
                button.setTitle("", forState: UIControlState.Normal)
                button.addTarget(self, action: #selector(PhotoViewController.buttonAction(_:)), forControlEvents: UIControlEvents.TouchUpInside)
                button.tag = Int(i[2][0])
                self.view.addSubview(button)
            }
        } catch {
            print(error)
        }
        
        spinner.hidden = true
        img.image = image
        img.hidden = false

    }
    
    func buttonAction(sender: UIButton!) {
        let button: UIButton = sender
        questionsMode = button.tag
        let vc = self.storyboard?.instantiateViewControllerWithIdentifier("questionsView")
        self.showViewController(vc!, sender: vc)
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        spinner.hidden = true
        img.hidden = true
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
    }
}

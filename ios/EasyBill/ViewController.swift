//
//  ViewController.swift
//  EasyBill
//
//  Created by Jacques LORENTZ on 29/06/2016.
//  Copyright © 2016 Jacques LORENTZ. All rights reserved.
//

import UIKit
import Starscream

var questionsJson: AnyObject = NSData()
let conn = SocketConnection()

class ViewController: UIViewController {

    @IBOutlet weak var spinner: UIActivityIndicatorView!
    @IBOutlet weak var photoButton: UIButton!
    @IBOutlet weak var questionButton: UIButton!
    
    var firstLoad: Bool = true
    
    override func viewDidLoad() {
        super.viewDidLoad()
        photoButton.hidden = true
        questionButton.hidden = true
        spinner.startAnimating()
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
    }

    override func viewDidAppear(animated: Bool) {
        questionsMode = -1
        if (firstLoad == true) {
            firstLoad = false
            
            conn.connect("192.168.2.10", port: 9090)
            
            do {
                questionsJson = try NSJSONSerialization.JSONObjectWithData(conn.getQuestions(), options: NSJSONReadingOptions())
            } catch {
                print(error)
            }
            
            self.spinner.stopAnimating()
            self.photoButton.hidden = false
            self.questionButton.hidden = false
            self.spinner.hidden = true
        }
    }
}


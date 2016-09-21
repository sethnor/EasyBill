//
//  ViewController.swift
//  EasyBill
//
//  Created by Jacques LORENTZ on 29/06/2016.
//  Copyright © 2016 Jacques LORENTZ. All rights reserved.
//

import UIKit

var questionsJson: AnyObject = Data() as AnyObject
let conn = SocketConnection()

class ViewController: UIViewController {

    @IBOutlet weak var spinner: UIActivityIndicatorView!
    @IBOutlet weak var photoButton: UIButton!
    @IBOutlet weak var questionButton: UIButton!
    
    var firstLoad: Bool = true
    
    override func viewDidLoad() {
        super.viewDidLoad()
        photoButton.isHidden = true
        questionButton.isHidden = true
        spinner.startAnimating()
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
    }

    override func viewDidAppear(_ animated: Bool) {
        questionsMode = -1
        questionPage = -1
        if (firstLoad == true) {
            firstLoad = false
            
            conn.connect("192.168.43.32", port: 80)
            //conn.connect("10.14.0.6", port: 2080)
            
            //conn.connect("esybill.strasbourg.epitech.eu", port: 1337)
            //conn.connect("10.14.60.28", port: 80)
            do {
                questionsJson = try JSONSerialization.jsonObject(with: conn.getQuestions() as Data, options: JSONSerialization.ReadingOptions()) as AnyObject
            } catch {
                print(error)
            }
            
            self.spinner.stopAnimating()
            self.photoButton.isHidden = false
            self.questionButton.isHidden = false
            self.spinner.isHidden = true
        }
    }
}


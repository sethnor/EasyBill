//
//  ResponseViewController.swift
//  EasyBill
//
//  Created by Jacques LORENTZ on 01/07/2016.
//  Copyright © 2016 Jacques LORENTZ. All rights reserved.
//

import UIKit

class ResponseViewController: UIViewController {

    @IBOutlet weak var question: UITextView!
    @IBOutlet weak var response: UITextView!
    override func viewDidLoad() {
        super.viewDidLoad()
        question.text = questionResponse[0]
        question.font = .systemFont(ofSize: 19)
        question.textAlignment = NSTextAlignment(rawValue: 1)!
        response.text = questionResponse[1]
        response.font = .systemFont(ofSize: 18)
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
    }
}

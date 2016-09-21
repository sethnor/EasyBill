//
//  QuestionsViewController.swift
//  EasyBill
//
//  Created by Jacques LORENTZ on 29/06/2016.
//  Copyright © 2016 Jacques LORENTZ. All rights reserved.
//

import UIKit

var questionsMode: Int = -1
var questionPage: Int = -1
var questionResponse: [String] = [String]()

class QuestionsViewController: UIViewController, UITableViewDelegate, UITableViewDataSource {

    @IBOutlet weak var test: UILabel!
    @IBOutlet weak var text: UITextView!
    @IBOutlet weak var table: UITableView!
    
    var data: [String] = [String]()
    var response: [String] = [String]()
    
    override func viewDidLoad() {
        super.viewDidLoad()
        self.title = (questionsMode == -1 ? "Question" : "Bloc")
        test.text = (questionsMode == -1 ? "Foire aux questions" : (questionsJson as! [[[String]]])[questionPage][questionsMode][0])
        text.text = (questionsMode == -1 ? "Ci dessous la liste des question aux quelles sont jointes leurs réponses." : (questionsJson as! [[[String]]])[questionPage][questionsMode][1])
        
        getData()
        table.delegate = self
        table.dataSource = self
        table.estimatedRowHeight = 50
        table.rowHeight = UITableViewAutomaticDimension
        table.reloadData()
        if (questionsJson.description != "") {
                
                //questionsJson.description.characters.count.description
        }
    }

    func getData() {
        if ((questionsJson as? [[[String]]]) == nil) {
            return
        }
        var i = 0
        for page in (questionsJson as! [[[String]]]) {
            if (i == questionPage || questionPage == -1) {
                var ii = 0
                for bloc in page {
                    if (ii == questionsMode || questionsMode == -1) {
                        var j = 0
                        for str in bloc {
                            if (j > 3 && j % 2 == 0) {
                                data.append(str)
                            } else if (j > 3 && j % 2 == 1) {
                                response.append(str)
                            }
                            j += 1
                        }
                    }
                    ii += 1
                }
            }
            i += 1
        }
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return data.count;
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: "cellSample", for: indexPath)
        cell.textLabel?.text = data[(indexPath as NSIndexPath).row]
        return cell
    }
   
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        questionResponse.removeAll()
        questionResponse.append(data[(indexPath as NSIndexPath).row])
        questionResponse.append(response[(indexPath as NSIndexPath).row])
        let vc = self.storyboard?.instantiateViewController(withIdentifier: "responseView")
        self.show(vc!, sender: vc)
    }
}

//
//  SocketConnection.swift
//  EasyBill
//
//  Created by Jacques LORENTZ on 30/06/2016.
//  Copyright © 2016 Jacques LORENTZ. All rights reserved.
//

import UIKit

class SocketConnection: NSObject, NSStreamDelegate {
    var host:String?
    var port:Int?
    var inputStream: NSInputStream?
    var outputStream: NSOutputStream?
    
    func connect(host: String, port: Int) {
        
        self.host = host
        self.port = port
        
        NSStream.getStreamsToHostWithName(host, port: port, inputStream: &inputStream, outputStream: &outputStream)
        
        if (inputStream != nil && outputStream != nil) {

            inputStream!.delegate = self
            outputStream!.delegate = self

            inputStream!.open()
            outputStream!.open()
            while (outputStream!.hasSpaceAvailable == false) {
                
            }
        }
    }
    
    func getQuestions() -> NSMutableData {
        
        let string = "UPDATE_0"
        let data = string.dataUsingEncoding(NSUTF8StringEncoding, allowLossyConversion: false)!
        outputStream!.write(UnsafePointer(data.bytes), maxLength: data.length)
   
        let res: NSMutableData = NSMutableData()
        while (inputStream!.hasBytesAvailable == false) {
            usleep(10000)
        }
        while (inputStream!.hasBytesAvailable){
            var buffer = [UInt8](count: 4096, repeatedValue: 0)
            let len = inputStream!.read(&buffer, maxLength: buffer.count)
            if(len > 0){
                res.appendData(NSData(bytes: buffer as [UInt8], length: len))
                usleep(10000)
            }
        }
        return (res);
    }
    
    func resizeImage(image: UIImage, newWidth: CGFloat) -> UIImage {
        
        let scale = newWidth / image.size.width
        let newHeight = image.size.height * scale
        UIGraphicsBeginImageContext(CGSizeMake(newWidth, newHeight))
        image.drawInRect(CGRectMake(0, 0, newWidth, newHeight))
        let newImage = UIGraphicsGetImageFromCurrentImageContext()
        UIGraphicsEndImageContext()
        
        return newImage
    }
    
    func sendImg(image: UIImage) -> NSData {
        let newImage = resizeImage(image, newWidth: 800)
        let data: NSData = UIImageJPEGRepresentation(newImage, 0.5)!
        var len: Int = 0
        while (len < data.length) {
            len += outputStream!.write(UnsafePointer(data.bytes + len), maxLength: data.length - len)
        }
        
        let res: NSMutableData = NSMutableData()
        while (inputStream!.hasBytesAvailable == false) {
            usleep(10000)
        }
        while (inputStream!.hasBytesAvailable){
            var buffer = [UInt8](count: 4096, repeatedValue: 0)
            let len = inputStream!.read(&buffer, maxLength: buffer.count)
            if(len > 0){
                res.appendData(NSData(bytes: buffer as [UInt8], length: len))
                usleep(10000)
            }
        }
        return (res)
    }
    
    func stream(aStream: NSStream, handleEvent eventCode: NSStreamEvent) {
        if aStream === inputStream {
            switch eventCode {
            case NSStreamEvent.ErrorOccurred:
                print("input: ErrorOccurred: \(aStream.streamError?.description)")
            case NSStreamEvent.OpenCompleted:
                print("input: OpenCompleted")
            case NSStreamEvent.HasBytesAvailable:
                print("input: HasBytesAvailable")
                
                // Here you can `read()` from `inputStream`
                
            default:
                break
            }
        }
        else if aStream === outputStream {
            switch eventCode {
            case NSStreamEvent.ErrorOccurred:
                print("output: ErrorOccurred: \(aStream.streamError?.description)")
            case NSStreamEvent.OpenCompleted:
                print("output: OpenCompleted")
            case NSStreamEvent.HasSpaceAvailable:
                print("output: HasSpaceAvailable")
                
                // Here you can write() to `outputStream`
                
            default:
                break
            }
        }
    }
}

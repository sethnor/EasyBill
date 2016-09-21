//
//  SocketConnection.swift
//  EasyBill
//
//  Created by Jacques LORENTZ on 30/06/2016.
//  Copyright © 2016 Jacques LORENTZ. All rights reserved.
//

import UIKit

class SocketConnection: NSObject, StreamDelegate {
    var host:String?
    var port:Int?
    var inputStream: InputStream?
    var outputStream: OutputStream?
    
    func connect(_ host: String, port: Int) {
        
        self.host = host
        self.port = port
        
        Stream.getStreamsToHost(withName: host, port: port, inputStream: &inputStream, outputStream: &outputStream)
        
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
        let data = string.data(using: String.Encoding.utf8, allowLossyConversion: false)!
        outputStream!.write((data as NSData).bytes.bindMemory(to: UInt8.self, capacity: data.count), maxLength: data.count)
   
        let res: NSMutableData = NSMutableData()
        while (inputStream!.hasBytesAvailable == false) {
            usleep(10000)
        }
        while (inputStream!.hasBytesAvailable){
            var buffer = [UInt8](repeating: 0, count: 4096)
            let len = inputStream!.read(&buffer, maxLength: buffer.count)
            if(len > 0){
                res.append(Data(bytes: UnsafePointer<UInt8>(buffer as [UInt8]), count: len))
                usleep(10000)
            }
        }
        return (res)
    }
    
    func resizeImage(_ image: UIImage, newWidth: CGFloat) -> UIImage {
        
        let scale = newWidth / image.size.width
        let newHeight = image.size.height * scale
        UIGraphicsBeginImageContext(CGSize(width: newWidth, height: newHeight))
        image.draw(in: CGRect(x: 0, y: 0, width: newWidth, height: newHeight))
        let newImage = UIGraphicsGetImageFromCurrentImageContext()
        UIGraphicsEndImageContext()
        
        return newImage!
    }
    
    func sendImg(_ image: UIImage) -> Data {
        let newImage = resizeImage(image, newWidth: 800)
        let data: Data = UIImageJPEGRepresentation(newImage, 0.5)!
        var len: Int = 0
        
        while (len < data.count) {
            var tmpLen: Int = 0
            tmpLen = outputStream!.write(((data as NSData).bytes).advanced(by: len).assumingMemoryBound(to: UInt8.self), maxLength: data.count - len)
            len += tmpLen
        }
        print("ok")
        let res: NSMutableData = NSMutableData()
        while (inputStream!.hasBytesAvailable == false) {
            usleep(10000)
        }
        while (inputStream!.hasBytesAvailable){
            var buffer = [UInt8](repeating: 0, count: 4096)
            let len = inputStream!.read(&buffer, maxLength: buffer.count)
            if(len > 0){
                res.append(Data(bytes: UnsafePointer<UInt8>(buffer as [UInt8]), count: len))
                usleep(10000)
            }
        }
        return (res) as Data
    }
    
    func stream(_ aStream: Stream, handle eventCode: Stream.Event) {
        if aStream === inputStream {
            switch eventCode {
            case Stream.Event.errorOccurred:
                print("input: ErrorOccurred")
            case Stream.Event.openCompleted:
                print("input: OpenCompleted")
            case Stream.Event.hasBytesAvailable:
                print("input: HasBytesAvailable")
                
                // Here you can `read()` from `inputStream`
                
            default:
                break
            }
        }
        else if aStream === outputStream {
            switch eventCode {
            case Stream.Event.errorOccurred:
                print("output: ErrorOccurred")
            case Stream.Event.openCompleted:
                print("output: OpenCompleted")
            case Stream.Event.hasSpaceAvailable:
                print("output: HasSpaceAvailable")
                
                // Here you can write() to `outputStream`
                
            default:
                break
            }
        }
    }
}

import Foundation


class RestApiRequest<T: Decodable> {
    let url: URL
    let httpMethod: HttpMethod
    let body: Encodable?
    
    init(url: URL, httpMethod: HttpMethod, body: Encodable? = nil) {
        self.url = url
        self.httpMethod = httpMethod
        self.body = body
    }
    
    func load(request: URLRequest, withCompletion completion: @escaping (T?) -> Void) {
        let task = URLSession.shared.dataTask(with: request) { (data, _ , _) -> Void in
            if let data = data {
                if let decodedResponse = try? JSONDecoder().decode(T.self, from: data) {
                    DispatchQueue.main.async {
                        completion(decodedResponse)
                    }
                }
            }
        }
        
        task.resume()
    }
    
    func execute(withCompletion completion: @escaping (T?) -> Void) {
        var request = URLRequest(url: url)
        request.httpMethod = httpMethod.rawValue
        
        if (body != nil) {
            request.httpBody = try? JSONEncoder().encode(body!)
        }
        
        request.setValue("application/json; charset=utf-8", forHTTPHeaderField: "Content-Type")
        request.setValue("application/json; charset=utf-8", forHTTPHeaderField: "Accept")
        
        load(request: request, withCompletion: completion)
    }
}

import Foundation


class RestApiRequest<T: Decodable> {
    let url: URL
    let httpMethod: HttpMethod
    let body: Encodable?
    let headers: Dictionary<String, String>?
    let timeout: Double
    
    init(url: URL, httpMethod: HttpMethod, headers: Dictionary<String, String>? = nil, body: Encodable? = nil, timeout: Double = 10.0) {
        self.url = url
        self.httpMethod = httpMethod
        self.body = body
        self.headers = headers
        self.timeout = timeout
    }
    
    func load(request: URLRequest) -> Call<T> {
        let response = Call<T>()
        
        let task = URLSession.shared.dataTask(with: request) { (data, _ , requestError) -> Void in
            if let data = data {
                do {
                    let decodedResponse = try JSONDecoder().decode(T.self, from: data)
                    response.body = decodedResponse
                } catch {
                    print(error)
                    response.error = error
                }
            } else {
                print(requestError.debugDescription)
                response.error = requestError
            }
            
            response.complete()
        }
        
        task.resume()
        
        return response
    }
    
    func execute() -> Call<T> {
        var request = URLRequest(url: url)
        request.httpMethod = httpMethod.rawValue
        
        if (body != nil) {
            request.httpBody = try? JSONEncoder().encode(body!)
        }
        
        if (headers != nil) {
            headers?.forEach({ request.setValue($1, forHTTPHeaderField: $0) })
        }
        
        request.setValue("application/json; charset=utf-8", forHTTPHeaderField: "Content-Type")
        request.setValue("application/json; charset=utf-8", forHTTPHeaderField: "Accept")
        request.timeoutInterval = timeout
        
        return load(request: request)
    }
}

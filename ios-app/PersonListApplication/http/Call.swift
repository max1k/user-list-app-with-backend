import Foundation


class Call<T: Decodable> {
    var body: T? = nil
    var error: Error? = nil
    
    private var resultHandler: ((T) -> Void)? = nil
    private var errorHandler: ((Error) -> Void)? = nil
    
    func onResult(_ resultHandler: @escaping (T) -> Void) -> Call<T> {
        self.resultHandler = resultHandler
        complete()
        
        return self
    }
    
    func onError(_ errorHandler: @escaping (Error) -> Void) -> Call<T> {
        self.errorHandler = errorHandler
        complete()
        
        return self
    }
    
    func complete() {
        if let body = body, let onResult = resultHandler {
            DispatchQueue.main.async {
                onResult(body)
            }
        } else if let error = error, let onError = errorHandler {
            DispatchQueue.main.async {
                onError(error)
            }
        }
    }
    
}

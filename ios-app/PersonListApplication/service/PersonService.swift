import Foundation


let personService = PersonService(baseUrl: "http://10.224.9.136:8088/person")

class PersonService {
    private let requestAll = "/all"
    
    private let baseUrl: String
    
    
    init(baseUrl: String) {
        self.baseUrl = baseUrl
    }
    
    
    func findAll(onLoad: @escaping ([Person]?) -> Void) {
        let request: RestApiRequest<[Person]> = RestApiRequest(url: URL(string: baseUrl + requestAll)!, httpMethod: .get)
        request.execute(withCompletion: onLoad)
    }
    
    func findById(personId: String, onLoad: @escaping (Person?) -> Void) {
        let request: RestApiRequest<Person> = RestApiRequest(url: URL(string: "\(baseUrl)/\(personId)")!, httpMethod: .get)
        request.execute(withCompletion: onLoad)
    }
    
    func delete(personId: String, onLoad: @escaping (Person?) -> Void) {
        let request: RestApiRequest<Person> = RestApiRequest(url: URL(string: "\(baseUrl)/\(personId)")!, httpMethod: .delete)
        request.execute(withCompletion: onLoad)
    }
    
    func save(person: Person, onLoad: @escaping (Person?) -> Void) {
        let request: RestApiRequest<Person> = RestApiRequest(url: URL(string: baseUrl)!, httpMethod: .post, body: person)
        request.execute(withCompletion: onLoad)
    }
    
}

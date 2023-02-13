import Foundation


let personService = PersonService(baseUrl: "http://192.168.31.146:8088/person")

class PersonService {
    private let requestAll = "/all"
    
    private let baseUrl: String
    
    
    init(baseUrl: String) {
        self.baseUrl = baseUrl
    }
    
    
    func findAll() -> Call<[Person]> {
        return RestApiRequest(url: URL(string: baseUrl + requestAll)!, httpMethod: .get)
            .execute()
    }
    
    func findById(personId: String) -> Call<Person> {
        return RestApiRequest(url: URL(string: "\(baseUrl)/\(personId)")!, httpMethod: .get)
            .execute()
    }
    
    func delete(personId: String) -> Call<Person> {
        return RestApiRequest(url: URL(string: "\(baseUrl)/\(personId)")!, httpMethod: .delete)
            .execute()
    }
    
    func save(person: Person) -> Call<Person> {
        return RestApiRequest(url: URL(string: baseUrl)!, httpMethod: .post, body: person)
            .execute()
    }
    
}

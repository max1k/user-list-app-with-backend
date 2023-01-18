import Foundation


class PersonDataModel: ObservableObject {
    @Published
    var person: Person
    
    @Published
    var isLoading: Bool
    
    
    init(person: Person, isLoading: Bool = false) {
        self.person = person
        self.isLoading = isLoading
    }
}

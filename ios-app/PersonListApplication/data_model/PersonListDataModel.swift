import Foundation


class PersonListDataModel: ObservableObject {
    @Published private(set) var personItems: [PersonDataModel] = []
    @Published private(set) var isLoading = false
    
    private var processingPersonIds: Set<String> = []
    private var persons: [Person] = []
    
    
    func getAllPersons() {
        guard !isLoading else { return }
        isLoading = true
        
        let call = personService.findAll()
        
        let _ = call.onResult { persons in
            self.persons = persons
            self.refreshItems()
            self.isLoading = false
        }
    }
    
    func movePerson(fromIndex: Int, toIndex: Int) {
        guard fromIndex >= 0 && fromIndex < persons.count else {
            return
        }
        
        guard toIndex >= 0 && toIndex < persons.count else {
            return
        }
        
        persons.swapAt(fromIndex, toIndex)
        refreshItems()
    }
    
    func deletePerson(person: Person) {
        let call = personService.delete(personId: person.id)
        let _ = call.onResult { deletedPerson in
            self.personItems.removeAll(where: { personItem in personItem.person.id == deletedPerson.id})
        }
    }
    
    func firePerson(person: Person) {
        let newPerson = Person(id: person.id, name: person.name, companyName: person.companyName, photo: person.photo, liked: person.liked, fired: !person.fired, active: person.active, details: person.details)
        
        updatePerson(newPerson)
    }
    
    func activatePerson(person: Person) {
        let newPerson = Person(id: person.id, name: person.name, companyName: person.companyName, photo: person.photo, liked: person.liked, fired: person.fired, active: !person.active, details: person.details)
        
        updatePerson(newPerson)
    }
    
    func likePerson(person: Person) {
        let newPerson = Person(id: person.id, name: person.name, companyName: person.companyName, photo: person.photo, liked: !person.liked, fired: person.fired, active: person.active, details: person.details)
        
        updatePerson(newPerson)
    }
    
    private func mapToItem(_ person: Person) -> PersonDataModel {
        return PersonDataModel(person: person, isLoading: processingPersonIds.contains(person.id))
    }
    
    private func findIndex(_ person: Person?) -> Int? {
        return persons.firstIndex(where: { $0.id == person?.id })
    }
    
    func updatePerson(_ person: Person) {
        processingPersonIds.insert(person.id)
        refreshItems()
        
        let call = personService.save(person: person)
        
        let _ = call.onResult { updatedPerson in
            guard let index = self.findIndex(updatedPerson)
            else {
                return
            }
            
            self.processingPersonIds.remove(updatedPerson.id)
            self.persons[index] = updatedPerson
            
            self.refreshItems()
        }
    }
    
    private func refreshItems() {
        personItems = persons.map(self.mapToItem)
    }
    
}

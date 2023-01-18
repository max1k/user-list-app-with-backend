import SwiftUI


struct PersonListView: View {
    @StateObject private var dataModel = PersonListDataModel()
    
    
    var body: some View {
        ZStack(alignment: .center) {
            Content()
                .opacity(dataModel.isLoading ? 0 : 1)
            
            ProgressView()
                .opacity(dataModel.isLoading ? 1 : 0)
        }
    }
    
    func Content() -> some View {
        List {
            ForEach(dataModel.personItems, id: \.person.id) { personItem in
                NavigationLink(destination: PersonDetailsView(person: personItem.person)) {
                    PersonDetails(item: personItem, dataModel: dataModel)
                }
            }
            .onMove(perform: move)
        }
        .navigationTitle("Peson list")
        .onAppear {
            dataModel.getAllPersons()
        }
    }
    
    func move(from source: IndexSet, to destination: Int) {
        guard let fromIndex = source.first else { return }
        
        dataModel.movePerson(fromIndex: fromIndex, toIndex: destination - 1)
    }
}

struct PersonDetails: View {
    let item: PersonDataModel
    let dataModel: PersonListDataModel
    
    
    var body: some View {
        HStack() {
            HStack {
                AsyncImage(url: URL(string: item.person.photo)) { image in
                    image.resizable()
                } placeholder: {
                    ProgressView()
                }
                    .frame(width: 48.0, height: 48.0)
                    .cornerRadius(8.0)
                
                VStack(alignment: .leading) {
                    Text(item.person.name)
                        .font(.headline)
                    Text(item.person.companyName)
                        .font(.subheadline)
                        .foregroundColor(.secondary)
                }
                .frame(maxWidth: .infinity, alignment: .leading)
                
            }
            .frame(maxWidth: .infinity, alignment: .leading)
            
            ZStack(alignment: .center) {
                HStack {
                    Image(systemName: item.person.fired ? "flame.fill" : "flame")
                        .foregroundColor(item.person.fired ? .orange : .secondary)
                        .onTapGesture {
                            fire(person: item.person)
                        }
                    
                    Image(systemName: item.person.active ? "checkmark.seal.fill" : "checkmark.seal")
                        .foregroundColor(item.person.active ? .blue : .secondary)
                        .onTapGesture {
                            activate(person: item.person)
                        }
                    
                    Image(systemName: item.person.liked ? "heart.fill" : "heart")
                        .foregroundColor(item.person.liked ? .red: .secondary)
                        .onTapGesture {
                            like(person: item.person)
                        }
                    
                }
                .frame(alignment: .trailing)
                .opacity(item.isLoading ? 0 : 1)
                
                ProgressView()
                    .opacity(item.isLoading ? 1 : 0)
            }
        }
        .padding(.top, 4.0)
        .padding(.bottom, 4.0)
        .contextMenu {
            Button(action: { fire(person: item.person) }) {
                Label("Fire", systemImage: "flame.fill")
            }
            
            Button(action: { activate(person: item.person) }) {
                Label("Activate", systemImage: "checkmark.seal.fill")
            }
            
            Button(action: { like(person: item.person) }) {
                Label("Like", systemImage: "heart.fill")
            }
            
            Button(action: { delete(person: item.person) }) {
                Label("Delete", systemImage: "person.crop.circle.fill.badge.minus")
            }
        }
    }
    
    func fire(person: Person) {
        dataModel.firePerson(person: person)
    }
    
    func activate(person: Person) {
        dataModel.activatePerson(person: person)
    }
    
    func like(person: Person) {
        dataModel.likePerson(person: person)
    }
    
    func delete(person: Person) {
        dataModel.deletePerson(person: person)
    }
}

struct PersonListView_Previews: PreviewProvider {
    static var previews: some View {
        PersonDetails(item: PersonDataModel(person: TestData.testPerson, isLoading: false), dataModel: PersonListDataModel())
    }
}

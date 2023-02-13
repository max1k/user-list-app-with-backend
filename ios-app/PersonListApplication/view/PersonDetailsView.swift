import Foundation
import SwiftUI


struct PersonDetailsView: View {
    @StateObject
    private var dataModel: PersonDataModel
    
    @State
    var isProcessing: Bool = false
    
    @Environment(\.presentationMode)
    var presentation
    
    init(person: Person) {
        let dataModel = PersonDataModel(person: person)
        _dataModel = StateObject(wrappedValue: dataModel)
    }
    
    var body: some View {
        GeometryReader { geometry in
            ZStack(alignment: .center) {

                Content()
                    .disabled(isProcessing)
                    .blur(radius: isProcessing ? 3 : 0)

                VStack {
                    Text("Processing...")
                    ProgressView()
                }
                .frame(width: geometry.size.width / 2,
                       height: geometry.size.height / 5)
                .background(Color.secondary.colorInvert())
                .foregroundColor(Color.primary)
                .cornerRadius(20)
                .opacity(isProcessing ? 1 : 0)

            }
        }
    }
    
    func Content() -> some View {
        VStack {
            AsyncImage(url: URL(string: dataModel.person.photo)) { image in
                image.resizable()
            } placeholder: {
                ProgressView()
            }
            .frame(width: 100.0, height: 100.0)
            .cornerRadius(8.0)
            .padding(.top, 16.0)
            
            Text(dataModel.person.name)
                .font(.headline)
                .padding(.top, 8.0)
            
            Text(dataModel.person.details)
                .padding(8.0)
            
            Button(action: { deletePerson(dataModel.person) }) {
                Label("Delete person", systemImage: "person.crop.circle.fill.badge.minus")
            }
            .padding(.top, 16.0)
            
            Spacer()
        }
        .frame(maxWidth: .infinity, alignment: .top)
    }
    
    func deletePerson(_ person: Person) {
        isProcessing = true
        
        let call = personService.delete(personId: person.id)
        let _ = call.onResult { _ in
            isProcessing = false
            presentation.wrappedValue.dismiss()
        }
    }
}

struct PersonDetalisView_Previews: PreviewProvider {
    static var previews: some View {
        PersonDetailsView(person: TestData.testPerson)
    }
}

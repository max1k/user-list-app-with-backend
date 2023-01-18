import Foundation


struct Person: Identifiable {
    let id: String
    let name: String
    let companyName: String
    let photo: String
    let liked: Bool
    let fired: Bool
    let active: Bool
    let details: String
}

extension Person: Decodable, Encodable {
    enum CodingKeys: String, CodingKey {
        case id, name, liked, fired, active, details
        case companyName = "company"
        case photo = "avatar"
    }
}

extension Person: CustomStringConvertible {
    var description: String {
        return "Person(id: \(id), name: '\(name)')"
    }
}

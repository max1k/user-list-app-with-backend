import Foundation


struct TestData {
    static let testPerson: Person = Person(
        id: UUID().uuidString,
        name: "Vassily Poupkine",
        companyName: "PPKN Brothers",
        photo: "https://randomuser.me/api/portraits/women/35.jpg",
        liked: true,
        fired: true,
        active: true,
        details: """
                 Architecto ut quis. Corrupti eum eos mollitia recusandae adipisci aut quia. Voluptatem ut et. Quae eum magni aspernatur totam quasi. Sint dolore porro inventore ipsam ut.
                 In quo est. Nihil sint eum commodi ea et quo vel. Rerum vel porro iusto. Accusamus suscipit consectetur.
                 Laudantium quidem voluptas ut magnam suscipit et veritatis. Saepe voluptatem quia. Explicabo in aut temporibus. Repellendus qui ducimus maxime doloribus.
                 """
    )
    
}

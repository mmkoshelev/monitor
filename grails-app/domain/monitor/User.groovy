package monitor

class User {

    String firstName
    String lastName
    Integer age

    static constraints = {
        firstName blank: false
        age nullable: false
    }
}

package pro.reiss.explore

// cats-ify the model (see shared-vanilla)
object domain:

  case class Actor(firstName: String, lastName: String):
    def fullName: String = s"$firstName $lastName"

  case class Director(firstName: String, lastName: String):
    def fullName: String = s"$firstName $lastName"

  object Director:
    // we're just going to assume that the full name is composed of two words
    private val fullnameR = """([A-Z])([a-z]+) ([A-Z])([a-z]+)""".r

    def fromString(s: String): Option[Director] =
      s match
        case fullnameR(first, last) => Some(Director(first, last))
        case _                      => None

  case class Movie(
      id: String,
      title: String,
      year: Int,
      actors: List[Actor],
      directors: List[Director]
  )

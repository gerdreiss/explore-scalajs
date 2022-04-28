package pro.reiss.explore

import zio.prelude.*
import java.time.LocalDate

object domain:
  enum ModelError:
    case EmptyValue, InvalidName, InvalidEmail, InvalidBirthdate

  object Firstname extends Subtype[String]:
    override inline def assertion =
      Assertion.hasLength(Assertion.greaterThan(1))

  object Lastname extends Subtype[String]:
    override inline def assertion =
      Assertion.hasLength(Assertion.greaterThan(1))

  object Email extends Subtype[String]:
    override inline def assertion =
      Assertion.matches("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$")

  object Birthdate extends Subtype[LocalDate]:
    override inline def assertion =
      val (minDate, maxDate) =
        LocalDate.now match
          case today: LocalDate =>
            today.minusYears(150) match
              case min: LocalDate => (min, today)
              case _              => ??? // this won't ever happen
          case _                => ??? // this won't happen either

      Assertion.greaterThan(minDate) && Assertion.lessThan(maxDate)

  type Firstname = Firstname.Type
  type Lastname  = Lastname.Type
  type Email     = Email.Type
  type Birthdate = Birthdate.Type

  final case class Person(
      firstname: Firstname,
      lastname: Lastname,
      email: Email,
      birthdate: Birthdate
  )

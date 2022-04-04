package pro.reiss.explore

import java.time.{ LocalDate, Month }
import scala.util.matching.Regex

object domain:
  enum ModelError:
    case EmptyValue, InvalidName, InvalidEmail, InvalidBirthdate

  opaque type Firstname = String
  object Firstname:
    def make(name: String): Either[ModelError, Firstname] =
      NameFactory.make(name)

  opaque type Lastname = String
  object Lastname:
    def make(name: String): Either[ModelError, Lastname] =
      NameFactory.make(name)

  private object NameFactory:
    def make(name: String): Either[ModelError, String] =
      if name.trim.nn.isEmpty then Left(ModelError.EmptyValue)
      else if name.length < 2 then Left(ModelError.InvalidName)
      else Right(name)

  opaque type Email = String
  object Email:
    private val emailR: Regex = "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$".r

    def make(email: String): Either[ModelError, Email] =
      if email.isEmpty then Left(ModelError.EmptyValue)
      else
        email match
          case emailR(v) => Right(v)
          case _         => Left(ModelError.InvalidEmail)

  opaque type Birthdate = LocalDate
  object Birthdate:
    def make(date: LocalDate): Either[ModelError, Birthdate] =
      val today = LocalDate.now.nn
      if date.isBefore(today.minusYears(120)) || date.isAfter(today) then Left(ModelError.InvalidBirthdate)
      else Right(date)

  final case class Person(
      firstname: Firstname,
      lastname: Lastname,
      email: Email,
      birthdate: Birthdate
  )

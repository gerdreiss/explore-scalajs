package pro.reiss.explore

import cats.*
import cats.effect.*
import cats.implicits.*
import io.circe.generic.auto.*
import io.circe.syntax.*
import org.http4s.*
import org.http4s.blaze.server.BlazeServerBuilder
import org.http4s.circe.*
import org.http4s.dsl.*
import org.http4s.dsl.impl.*
import org.http4s.headers.*
import org.http4s.implicits.*
import org.http4s.server.*
import pro.reiss.explore.domain.*

import java.time.Year
import scala.collection.mutable
import scala.util.Try

object ExploreHttp4s extends App:

  val directorDb: mutable.Map[String, Director] = mutable.Map(
    "Zack Snyder" -> Director("Zack", "Snyder")
  )

  /**
   * - Get all movies for a director
   * - Get all actors for a movie
   * - Get on director
   * - Post a new director
   */

  // Request -> F[Option[Response]] = HttpRoutes[F]

  given QueryParamDecoder[Year] = QueryParamDecoder[Int].map(y => Year.of(y).nn)

  object DirectorQueryParamMatcher extends QueryParamDecoderMatcher[String]("director")
  object YearQueryParamMatcher     extends OptionalQueryParamDecoderMatcher[Year]("year")

  // Get /movies?director=<director>&year=<year>
  def movieRoutes[F[_]: Monad]: HttpRoutes[F] =
    val dsl = Http4sDsl[F]
    import dsl.*

    HttpRoutes.of[F] {
      case GET -> Root / "movies" :? DirectorQueryParamMatcher(director) +& YearQueryParamMatcher(year) =>
        ???
      case GET -> Root / "movies" / UUIDVar(movieId) / "actors"                                         =>
        ???
    }

  object DirectorPath:
    def unapply(s: String): Option[Director] =
      Director.fromString(s)

  def directorRoutes[F[_]: Monad]: HttpRoutes[F] =
    val dsl = Http4sDsl[F]
    import dsl.*

    HttpRoutes.of[F] {
      case POST -> Root / "directors" / DirectorPath(director)        =>
        ???
      case GET -> Root / "directors" / UUIDVar(directorId) / "movies" =>
        ???
    }

  def allRoutes[F[_]: Monad]: HttpApp[F] = (movieRoutes[F] <+> directorRoutes[F]).orNotFound

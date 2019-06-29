package pine

import org.scalacheck.{Gen, Properties}
import org.scalacheck.Prop.forAll

import scala.collection.mutable.ListBuffer

class NodePropSpec extends Properties("Node") {
  val attributeKeyChar = Gen.oneOf('a', '1', '-', '_', 'ä')
  val attributeKeyGen  = Gen.listOf(attributeKeyChar).map(_.mkString)
    .filter(_.nonEmpty)
    .filter(x => x.head.isLetter || x.head == '_')
  val attributeValueChar = Gen.oneOf('a', '1', 'ö', '&', ';', '\'', '"', '-', ' ')
  val attributeValueGen  = Gen.listOf(attributeValueChar).map(_.mkString)

  val attribute = for {
    k <- attributeKeyGen
    v <- attributeValueGen
  } yield (k, v)

  val textGen = for {
    s <- attributeValueGen
  } yield Text(s)

  def tagGen(sz: Int): Gen[Tag[_]] = tagGen(sz, List.empty)

  def tagGen(sz: Int, parentTags: List[String]): Gen[Tag[_]] =
    for {
      // TODO Consider nesting rules (tag.Input cannot have children)
      tag <- Gen.oneOf("a", "b", "div", "span").filter { t =>
        if (Set("a", "b").contains(t)) !parentTags.contains(t)
        else true
      }
      attributes <- Gen.mapOfN(sz / 3, attribute)

      n <- Gen.choose(sz / 5, sz / 2)
      children <- Gen.listOfN(n, sizedTree(sz / 2, parentTags ++ List(tag))).filter { l =>
        l.length <= 1 || !l.zip(l.tail).exists {
          // Two adjacent nodes cannot be text nodes
          case (left, right) =>
            left.isInstanceOf[Text] &&
              right.isInstanceOf[Text]
        }
      }.filter { l =>
        // Ignore empty text nodes
        !l.exists {
          case t: Text => t.text.isEmpty
          case _ => false
        }
      }
    } yield Tag(tag.asInstanceOf[String with Singleton], attributes, children)

  def sizedTree(sz: Int, parentTags: List[String]): Gen[Node] =
    if (sz <= 0) textGen
    else Gen.frequency((1, textGen), (3, tagGen(sz, parentTags)))

  val sized = Gen.choose(0, 20)

  def rootTagGen: Gen[Tag[_]] = sized.flatMap(tagGen)

  def fun1: Node => Boolean = {
    case Tag(_, _, _) => true
    case _            => false
  }

  def fun2: Node => Boolean = {
    case _: Text => true
    case _       => false
  }

  def filterFunGen: Gen[Node => Boolean] = Gen.oneOf(fun1, fun2)

  property("toHtml") = forAll(rootTagGen) { tag: Tag[_] =>
    HtmlParser.fromString(tag.toHtml) == tag
  }

  def myFilter(node: Tag[_], f: Node => Boolean): List[Node] = {
    val collected = ListBuffer.empty[Node]
    def iter(node: Node): Unit = {
      if (f(node)) collected += node
      node match {
        case t @ Tag(_, _, _) => t.children.foreach(iter)
        case _ =>
      }
    }
    node.children.foreach(iter)
    collected.toList
  }

  property("filter (forall)") = forAll(sized.flatMap(tagGen), filterFunGen) { (tag, f) =>
    tag.filter(f).forall(f)
  }

  property("filter (reference implementation)") = forAll(sized.flatMap(tagGen), filterFunGen) { (tag, f) =>
    tag.filter(f) == myFilter(tag, f)
  }

  property("toText") = forAll(sized.flatMap(tagGen)) { tag: Tag[_] =>
    val text = tag.toText
    tag
      .filter(_.isInstanceOf[Text])
      .forall(x => text.contains(x.asInstanceOf[Text].text.trim.replaceAll("\\s+", " ")))
  }

  /*
    property("toText (DOM)") = forAll(nodeGen) { node: Node =>
      node.toText == node.toDom.textContent
    }
  }*/
}

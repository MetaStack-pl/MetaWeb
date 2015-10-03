package pl.metastack.metaweb.tag

import pl.metastack.metarx._
import pl.metastack.metaweb.state

/**
 * The HTML basefont element (<code>&lt;basefont&gt;</code>) establishes a default font size for a document. Font size then can be varied relative to the base font size using the <a href="/en-US/docs/Web/HTML/Element/font" title="The HTML Font Element (<font>) defines the font size, color and face for its content."><code>&lt;font&gt;</code></a> element.
<p><em>Usage note: </em></p> 
<p><strong>Do not use this element! </strong>Though once (imprecisely) normalized in HTML 3.2, it wasn't supported in all major browsers. Further, browsers, and even successive versions of browsers, never implemented it in the same way: practically, using it has always brought <em>indeterminate</em> results.</p> 
<p>The <code>&lt;basefont&gt; </code>element was deprecated in the standard at the same time as all elements related to styling only. Starting with HTML 4, HTML does not convey styling information anymore (outside the <a href="/en-US/docs/Web/HTML/Element/style" title="The HTML <style> element contains style information for a document, or a part of document. The specific style information is contained inside of this element, usually in the CSS."><code>&lt;style&gt;</code></a> element or the <strong>style</strong> attribute of each element). In HTML5, this element has been removed completely. For any new web development, styling should be written using <a href="/en-US/docs/CSS" title="CSS">CSS</a> only.</p> 
<p>The former behavior of the <a href="/en-US/docs/Web/HTML/Element/font" title="The HTML Font Element (<font>) defines the font size, color and face for its content."><code>&lt;font&gt;</code></a> element can be achieved, and even better controlled using the <a href="/en-US/docs/Web/CSS/font" title=""><code>font</code></a>, <a href="/en-US/docs/Web/CSS/font-family" title=""><code>font-family</code></a>, <a href="/en-US/docs/Web/CSS/font-size" title=""><code>font-size</code></a>, <a href="/en-US/docs/Web/CSS/font-size-adjust" title=""><code>font-size-adjust</code></a>, <a href="/en-US/docs/Web/CSS/font-stretch" title=""><code>font-stretch</code></a>, <a href="/en-US/docs/Web/CSS/font-style" title=""><code>font-style</code></a>, <a href="/en-US/docs/Web/CSS/font-variant" title=""><code>font-variant</code></a>, <a href="/en-US/docs/Web/CSS/font-weight" title=""><code>font-weight</code></a>, <a href="/en-US/docs/Web/CSS/@font-face" title=""><code>@font-face</code></a> properties.</p>
 */
class Basefont extends state.Tag("basefont") with HTMLTag {
  /**
   * This attribute sets the text color using either a named color or a color specified in the hexadecimal #RRGGBB format.
   */
  def color: StateChannel[String] = attribute("color").asInstanceOf[StateChannel[String]]
  /**
   * This attribute contains a list of one or more font names. The document text in the default style is rendered in the first font face that the client's browser supports. If no font listed is installed on the local system, the browser typically defaults to the proportional or fixed-width font for that system.
   */
  def face: StateChannel[String] = attribute("face").asInstanceOf[StateChannel[String]]
  /**
   * This attribute specifies the font size as either a numeric or relative value. Numeric values range from 1 to 7 with 1 being the smallest and 3 the default.
   */
  def size: StateChannel[String] = attribute("size").asInstanceOf[StateChannel[String]]
}
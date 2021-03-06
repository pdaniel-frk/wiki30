package org.xwiki.gwt.wysiwyg.client.plugin.rt;

import org.xwiki.gwt.dom.client.Range;
import org.xwiki.gwt.wysiwyg.client.RtPluginTestCase;

import com.google.gwt.dom.client.Node;

/**
 * Tests how the caret is normalized when there are headings in the document
 * @author Bogdan.Flueras@inria.fr
 */
public class CaretNormalizationHeadingsTest extends RtPluginTestCase
{
    public static final String INNER_HTML = "abc";

    private Range oldCaretPos;

    @Override
    public void gwtSetUp() throws Exception
    {
        super.gwtSetUp();
        oldCaretPos = getDocument().createRange();
        container = getDocument().createHElement(1).cast();
        container.setInnerHTML(INNER_HTML);
        getDocument().getBody().replaceChild(container, getContainer());
        assertTrue("Invalid tag name", "h1".equalsIgnoreCase(getContainer().getNodeName()));
    }

    public void testCaretInsideHeadingBeforeTextNode()
    {
        oldCaretPos.setStart(container, 0);
        oldCaretPos.setEnd(container, 0);

        Range newCaretPos = EditorUtils.normalizeCaretPosition(oldCaretPos);
        assertNotNull(newCaretPos);
        assertEquals(Node.TEXT_NODE, newCaretPos.getStartContainer().getNodeType());
        assertEquals(container.getFirstChild(), newCaretPos.getStartContainer());
        assertEquals(0, newCaretPos.getStartOffset());
        assertTrue(newCaretPos.isCollapsed());
    }

    public void testCaretInsideHeadingAfterTextNode()
    {
        oldCaretPos.setStart(container, 1);
        oldCaretPos.setEnd(container, 1);

        Range newCaretPos = EditorUtils.normalizeCaretPosition(oldCaretPos);
        assertNotNull(newCaretPos);
        assertEquals(Node.TEXT_NODE, newCaretPos.getStartContainer().getNodeType());
        assertEquals(container.getFirstChild(), newCaretPos.getStartContainer());
        assertEquals(container.getFirstChild().getNodeValue().length(), newCaretPos.getStartOffset());
        assertTrue(newCaretPos.isCollapsed());
    }
}

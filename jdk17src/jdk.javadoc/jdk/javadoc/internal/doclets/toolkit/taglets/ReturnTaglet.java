/*
 * Copyright (c) 2001, 2020, Oracle and/or its affiliates. All rights reserved.
 * ORACLE PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 */

package jdk.javadoc.internal.doclets.toolkit.taglets;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.type.TypeMirror;

import com.sun.source.doctree.DocTree;
import com.sun.source.doctree.ReturnTree;
import jdk.javadoc.doclet.Taglet.Location;
import jdk.javadoc.internal.doclets.toolkit.Content;
import jdk.javadoc.internal.doclets.toolkit.Messages;
import jdk.javadoc.internal.doclets.toolkit.util.CommentHelper;
import jdk.javadoc.internal.doclets.toolkit.util.DocFinder;
import jdk.javadoc.internal.doclets.toolkit.util.DocFinder.Input;
import jdk.javadoc.internal.doclets.toolkit.util.Utils;

/**
 * A taglet that represents the {@code @return} and {@code {@return }} tags.
 *
 *  <p><b>This is NOT part of any supported API.
 *  If you write code that depends on this, you do so at your own risk.
 *  This code and its internal interfaces are subject to change or
 *  deletion without notice.</b>
 */
public class ReturnTaglet extends BaseTaglet implements InheritableTaglet {

    public ReturnTaglet() {
        super(DocTree.Kind.RETURN, true, EnumSet.of(Location.METHOD));
    }

    @Override
    public boolean isBlockTag() {
        return true;
    }

    @Override
    public void inherit(DocFinder.Input input, DocFinder.Output output) {
        Utils utils = input.utils;
        CommentHelper ch = utils.getCommentHelper(input.element);

        ReturnTree tag = null;
        List<? extends ReturnTree> tags = utils.getReturnTrees(input.element);
        if (!tags.isEmpty()) {
            tag = tags.get(0);
        } else {
            List<? extends DocTree> firstSentence = utils.getFirstSentenceTrees(input.element);
            if (firstSentence.size() == 1 && firstSentence.get(0).getKind() == DocTree.Kind.RETURN) {
                tag = (ReturnTree) firstSentence.get(0);
            }
        }

        if (tag != null) {
            output.holder = input.element;
            output.holderTag = tag;
            output.inlineTags = input.isFirstSentence
                    ? ch.getFirstSentenceTrees(output.holderTag)
                    : ch.getDescription(output.holderTag);
        }
    }

    @Override
    public Content getInlineTagOutput(Element element, DocTree tag, TagletWriter writer) {
        return writer.returnTagOutput(element, (ReturnTree) tag, true);
    }

    @Override
    public Content getAllBlockTagOutput(Element holder, TagletWriter writer) {
        Messages messages = writer.configuration().getMessages();
        Utils utils = writer.configuration().utils;
        List<? extends ReturnTree> tags = utils.getReturnTrees(holder);

        // Make sure we are not using @return tag on method with void return type.
        TypeMirror returnType = utils.getReturnType(writer.getCurrentPageElement(), (ExecutableElement)holder);
        if (returnType != null && utils.isVoid(returnType)) {
            if (!tags.isEmpty()) {
                messages.warning(holder, "doclet.Return_tag_on_void_method");
            }
            return null;
        }

        if (!tags.isEmpty()) {
            return writer.returnTagOutput(holder, tags.get(0), false);
        }

        // Check for inline tag in first sentence.
        List<? extends DocTree> firstSentence = utils.getFirstSentenceTrees(holder);
        if (firstSentence.size() == 1 && firstSentence.get(0).getKind() == DocTree.Kind.RETURN) {
            return writer.returnTagOutput(holder, (ReturnTree) firstSentence.get(0), false);
        }

        // Inherit @return tag if necessary.
        Input input = new DocFinder.Input(utils, holder, this);
        DocFinder.Output inheritedDoc = DocFinder.search(writer.configuration(), input);
        if (inheritedDoc.holderTag != null) {
            return writer.returnTagOutput(inheritedDoc.holder, (ReturnTree) inheritedDoc.holderTag, false);
        }
        return null;
    }
}

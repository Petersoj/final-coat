package net.jacobpeterson.finalcoat;

import com.google.auto.service.AutoService;
import com.google.errorprone.BugPattern;
import com.google.errorprone.VisitorState;
import com.google.errorprone.bugpatterns.BugChecker;
import com.google.errorprone.bugpatterns.BugChecker.VariableTreeMatcher;
import com.google.errorprone.fixes.SuggestedFix;
import com.google.errorprone.matchers.Description;
import com.sun.source.tree.VariableTree;

import static com.google.errorprone.BugPattern.SeverityLevel.WARNING;
import static com.google.errorprone.BugPattern.StandardTags.STYLE;
import static com.google.errorprone.matchers.Description.NO_MATCH;
import static com.google.errorprone.util.ASTHelpers.getSymbol;
import static com.sun.source.tree.Tree.Kind.INTERFACE;
import static com.sun.source.tree.Tree.Kind.LAMBDA_EXPRESSION;
import static com.sun.source.tree.Tree.Kind.METHOD;
import static com.sun.tools.javac.code.Flags.EFFECTIVELY_FINAL;
import static javax.lang.model.element.Modifier.FINAL;

@AutoService(BugChecker.class)
@BugPattern(
        summary = "Use `final` keyword on effectively final variables",
        severity = WARNING,
        tags = STYLE
)
public class FinalCoat extends BugChecker implements VariableTreeMatcher {

    @Override
    public Description matchVariable(final VariableTree tree, final VisitorState state) {
        final var symbol = getSymbol(tree);
        if (symbol.getModifiers().contains(FINAL)) {
            return NO_MATCH;
        }
        final var treePath = state.getPath();
        if (treePath != null) {
            final var treePathParent = treePath.getParentPath();
            if (treePathParent != null) {
                final var treePathParentKind = treePathParent.getLeaf().getKind();
                // `final` keyword cannot be applied to lambda expression variables.
                if (treePathParentKind == LAMBDA_EXPRESSION) {
                    return NO_MATCH;
                }
                // `final` keyword on interface methods.
                if (treePathParentKind == METHOD && treePathParent.getParentPath().getLeaf().getKind() == INTERFACE) {
                    return match(tree);
                }
            }
        }
        if ((symbol.flags() & EFFECTIVELY_FINAL) == 0) {
            return NO_MATCH;
        }
        return match(tree);
    }

    private Description match(final VariableTree tree) {
        return describeMatch(tree, SuggestedFix.builder().prefixWith(tree, "final ").build());
    }
}

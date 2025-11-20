package net.jacobpeterson.finalcoat;

import com.google.auto.service.AutoService;
import com.google.errorprone.BugPattern;
import com.google.errorprone.VisitorState;
import com.google.errorprone.bugpatterns.BugChecker;
import com.google.errorprone.bugpatterns.BugChecker.VariableTreeMatcher;
import com.google.errorprone.fixes.SuggestedFix;
import com.google.errorprone.matchers.Description;
import com.sun.source.tree.VariableTree;
import com.sun.source.util.TreePath;
import com.sun.tools.javac.code.Symbol;

import static com.google.errorprone.BugPattern.SeverityLevel.WARNING;
import static com.google.errorprone.BugPattern.StandardTags.STYLE;
import static com.google.errorprone.matchers.Description.NO_MATCH;
import static com.google.errorprone.util.ASTHelpers.getSymbol;
import static com.sun.source.tree.Tree.Kind.LAMBDA_EXPRESSION;
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
        final Symbol symbol = getSymbol(tree);
        if (symbol.getModifiers().contains(FINAL) || (symbol.flags() & EFFECTIVELY_FINAL) == 0) {
            return NO_MATCH;
        }
        // `final` keyword cannot be applied to lambda expression variables.
        for (TreePath treePath = state.getPath(); treePath != null; treePath = treePath.getParentPath()) {
            if (treePath.getLeaf().getKind() == LAMBDA_EXPRESSION) {
                return NO_MATCH;
            }
        }
        return describeMatch(tree, SuggestedFix.builder().prefixWith(tree, "final ").build());
    }
}

package edu.depaul.cdm.cpdt.editor.codeassist;

import org.eclipse.jface.text.rules.IPredicateRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.MultiLineRule;
import org.eclipse.jface.text.rules.RuleBasedPartitionScanner;
import org.eclipse.jface.text.rules.Token;



public class MADLPartitionScanner extends RuleBasedPartitionScanner {
	public final static String MADL_DEFAULT = "__madl_default";
	public final static String MADL_COMMENT = "__madl_comment";
	public final static String MADL_TAG = "__madl_tag";

	public MADLPartitionScanner() {

		IToken madlComment = new Token(MADL_COMMENT);
		IToken tag = new Token(MADL_TAG);

		IPredicateRule[] rules = new IPredicateRule[2];

		rules[0] = new MultiLineRule("/*", "*/", madlComment);
		rules[1] = new TagRule(tag);

		setPredicateRules(rules);
	}
}

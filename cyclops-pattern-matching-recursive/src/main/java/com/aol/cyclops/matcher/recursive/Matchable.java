package com.aol.cyclops.matcher.recursive;

import java.util.function.Function;

import com.aol.cyclops.matcher.builders.CheckType;
import com.aol.cyclops.matcher.builders.CheckTypeAndValues;
import com.aol.cyclops.matcher.builders.CheckValues;
import com.aol.cyclops.matcher.builders.MatchingInstance;
import com.aol.cyclops.matcher.builders.PatternMatcher;
import com.aol.cyclops.matcher.builders.RecursiveMatcherInstance;
import com.aol.cyclops.matcher.builders._Simpler_Case;


/**
 * Matchable
 * 
 * todo - add AsMatchable.asMatchable
 * 
 * @author johnmcclean
 *
 */
public interface Matchable{
	
	
	/**
	 * @return matchable
	 */
	default Object getMatchable(){
		return this;
	}
	/**
	 * <pre>{@code 
	 *   new MyCase(1,2,3).matchValues(this::choseMessage)  
	 *   
	 *   private <I,T> CheckValues<Object, T> chooseMessage(CheckValues<I, T> c) {
		      return c.with(1,2,3).then(i->"hello")
				      .with(4,5,6).then(i->"goodbye");
	     }
	     \@Value static class MyCase  implements Matchable{ int a; int b; int c; }
	 * }</pre>
	 * 
	 * @param fn
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	default <R,T,I> R  match(Function<CheckValues<I,T>,CheckValues<I,T>> fn){
		
		return (R) new MatchingInstance(new _Simpler_Case( fn.apply( (CheckValues)
					new _Simpler_Case(new PatternMatcher()).withType(getMatchable().getClass())).getPatternMatcher()))
						.match(getMatchable()).get();
	}
	@SuppressWarnings({ "rawtypes", "unchecked" })
	default <R,T,I> R  match(Function<CheckValues<I,T>,CheckValues<I,T>> fn, R defaultValue){
		
		return (R) new MatchingInstance(new _Simpler_Case( fn.apply( (CheckValues)
					new _Simpler_Case(new PatternMatcher()).withType(getMatchable().getClass())).getPatternMatcher()))
						.match(getMatchable()).orElse(defaultValue);
	}
	/**
	 * Match against this matchable using simple matching interface
	 * 
	 * <pre>{@code 
	 * return match(c -> 
						c.caseOf( (Put p) -> new Put(p.key,p.value,(Action)fn.apply(p.next)))
						 .caseOf((Delete d) -> new Delete(d.key,(Action)fn.apply(d.next)))
						 .caseOf((Get g) -> new Get(g.key,(Function)g.next.andThen(fn)))
					);
	 * }</pre>
	 * 
	 * @param fn Function to build the matching expression
	 * @return Matching result
	 */
	default <R> R matchType(Function<CheckType<? super R>,CheckType<? super R>> fn){
		return new MatchingInstance<Object,R>(fn.apply( new CheckType<>( new PatternMatcher()))).match(getMatchable()).get();
	} 
	/**
	 * Match against this matchable using simple matching interface
	 * 
	 * <pre>{@code 
	 * return match(c -> 
						 c.caseOf( (Put p) -> new Put(p.key,p.value,(Action)fn.apply(p.next)))
						 .caseOf((Delete d) -> new Delete(d.key,(Action)fn.apply(d.next)))
						 .caseOf((Get g) -> new Get(g.key,(Function)g.next.andThen(fn)))
						 noOperation()
					);
	 * }</pre>
	 * 
	 * @param fn Function to build the matching expression
	 * @param defaultValue Default value if matching expression does not match
	 * @return Matching result
	 */
	default <R> R matchType(Function<CheckType<? super R>,CheckType<? super R>> fn,R defaultValue){
		return new MatchingInstance<Object,R>(fn.apply( new CheckType<>( new PatternMatcher()))).match(getMatchable()).orElse(defaultValue);
	}
	/**
	 * Match against this matchable using algebraic matching interface (each field can
	 * be matched individually).
	 * 
	 * 
	 * @param fn Function to build the matching expression
	 * @return Matching result
	 */
	default <R,I> R _match(Function<CheckTypeAndValues<I>,RecursiveMatcherInstance> fn){
		return (R)fn.apply( RecursiveMatcher.when()).match(getMatchable()).get();
	}
	
	/**
	 * Match against this matchable using algebraic matching interface (each field can
	 * be matched individually).
	 * 
	 * 
	 * @param fn Function to build the matching expression
	 * @param defaultValue Default value if matching expression does not match
	 * @return Matching result
	 */
	default <R,I> R _match(Function<CheckTypeAndValues<I>,RecursiveMatcherInstance> fn,R defaultValue){
		return (R)fn.apply( RecursiveMatcher.when()).match(getMatchable()).orElse(defaultValue);
	}
}

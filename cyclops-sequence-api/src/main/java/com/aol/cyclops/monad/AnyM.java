package com.aol.cyclops.monad;

import java.io.BufferedReader;
import java.io.File;
import java.net.URL;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.OptionalLong;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.BaseStream;
import java.util.stream.Collector;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

import com.aol.cyclops.sequence.Monoid;
import com.aol.cyclops.sequence.SequenceM;
import com.aol.cyclops.sequence.Unwrapable;
import com.aol.cyclops.sequence.streamable.Streamable;

/**
 * 
 * Wrapper for Any Monad type
 * @see AnyMonads companion class for static helper methods
 * 
 * @author johnmcclean
 *
 * @param <T>
 */

public interface AnyM<T> extends Unwrapable{
	
	
	
	/**
	 * Construct an AnyM instance that wraps a range from start (inclusive) to end (exclusive) provided
	 * 
	 * The AnyM will contain a SequenceM over the spefied range
	 * 
	 * @param start Inclusive start of the range
	 * @param end Exclusive end of the range
	 * @return AnyM range
	 */
	public static AnyM<Integer> fromRange(int start, int end){
		
		return AnyM.fromStream(SequenceM.range(start, end));
	}
	/**
	 * Wrap a Streamable inside an AnyM
	 * 
	 * @param streamable wrap
	 * @return
	 */
	public static <T> AnyM<T> fromStreamable(Streamable<T> streamable){
		 Objects.requireNonNull(streamable);
		return AnyMFactory.instance.monad(streamable);
	}
	/**
	 * Create an AnyM from a List
	 * 
	 * This AnyM will convert the List to a Stream under the covers, but will rematerialize the Stream as List
	 * if wrap() is called
	 * 
	 * 
	 * @param list to wrap inside an AnyM
	 * @return AnyM wrapping a list
	 */
	public static <T> AnyM<T> fromList(List<T> list){
		 Objects.requireNonNull(list);
		return AnyMFactory.instance.monad(list);
	}
	/**
	 * Create an AnyM from a Set
	 * 
	 * This AnyM will convert the Set to a Stream under the covers, but will rematerialize the Stream as Set
	 * if wrap() is called
	 * 
	 * 
	 * @param list to wrap inside an AnyM
	 * @return AnyM wrapping a Set
	 */
	public static <T> AnyM<T> fromSet(Set<T> set){
		 Objects.requireNonNull(set);
		return AnyMFactory.instance.monad(set);
	}
	
	
	/**
	 * Create an AnyM wrapping a Stream of the supplied data
	 * 
	 * @param streamData
	 * @return
	 */
	public static <T> AnyM<T> fromArray(T... streamData){
		return AnyMFactory.instance.monad(Stream.of(streamData));
	}
	
	public static <T> AnyM<T> fromStream(Stream<T> stream){
		Objects.requireNonNull(stream);
		return AnyMFactory.instance.monad(stream);
	}
	public static AnyM<Integer> fromIntStream(IntStream stream){
		Objects.requireNonNull(stream);
		return AnyMFactory.instance.monad(stream);
	}
	public static AnyM<Double> fromDoubleStream(DoubleStream stream){
		Objects.requireNonNull(stream);
		return AnyMFactory.instance.monad(stream);
	}
	public static AnyM<Long> fromLongStream(LongStream stream){
		Objects.requireNonNull(stream);
		return AnyMFactory.instance.monad(stream);
	}
	public static <T> AnyM<T> fromOptional(Optional<T> optional){
		 Objects.requireNonNull(optional);
		return AnyMFactory.instance.monad(optional);
	}
	public static  AnyM<Double> fromOptional(OptionalDouble optional){
		Objects.requireNonNull(optional);
		return AnyMFactory.instance.of(optional);
	}
	public static  AnyM<Long> fromOptional(OptionalLong optional){
		Objects.requireNonNull(optional);
		return AnyMFactory.instance.of(optional);
	}
	public static  AnyM<Integer> fromOptional(OptionalInt optional){
		Objects.requireNonNull(optional);
		return AnyMFactory.instance.of(optional);
	}
	public static <T> AnyM<T> fromCompletableFuture(CompletableFuture<T> future){
		Objects.requireNonNull(future);
		return AnyMFactory.instance.monad(future);
	}
	public static <T> AnyM<T> fromCollection(Collection<T> collection){
		Objects.requireNonNull(collection);
		return AnyMFactory.instance.of(collection);
	}
	public static <T> AnyM<T> fromIterable(Iterable<T> iterable){
		Objects.requireNonNull(iterable);
		return AnyMFactory.instance.of(iterable);
	}
	public static AnyM<String> fromFile(File file){
		Objects.requireNonNull(file);
		return AnyMFactory.instance.of(file);
	}
	public static AnyM<String> fromURL(URL url){
		Objects.requireNonNull(url);
		return AnyMFactory.instance.of(url);
	}
	/**
	 * Take the supplied object and always attempt to convert it to a Monad type
	 * 
	 * @param monad
	 * @return
	 */
	public static <T> AnyM<T> ofConvertable(Object monad){
		Objects.requireNonNull(monad);
		return AnyMFactory.instance.of(monad);
	}
	/**
	 * Take the supplied object and wrap it inside an AnyM - must be a supported monad type already
	 * 
	 * @param monad to wrap
	 * @return Wrapped Monad
	 */
	public static <T> AnyM<T> ofMonad(Object monad){
		Objects.requireNonNull(monad);
		return AnyMFactory.instance.monad(monad);
	}
	public static <T> AnyM<T> ofNullable(Object monad){
		return AnyMFactory.instance.monad(Optional.ofNullable(monad));
	}
	
	
	 /* 
	  * Unwraps the wrapped monad, in it's current state.
	  * i.e. Lists or Sets may be Streams
	  * (non-Javadoc)
	 * @see com.aol.cyclops.sequence.Unwrapable#unwrap()
	 */
	<R> R unwrap();
	
	
	
	

	 <X extends Object> X monad();
	
	   AnyM<T>  filter(Predicate<? super T> fn);
	/* (non-Javadoc)
	 * @see com.aol.cyclops.lambda.monads.Functor#map(java.util.function.Function)
	 */
	  <R> AnyM<R> map(Function<? super T,? extends R> fn);
	/* (non-Javadoc)
	 * @see com.aol.cyclops.lambda.monads.Functor#peek(java.util.function.Consumer)
	 */
	   AnyM<T>  peek(Consumer<? super T> c) ;
	
	
	/**
	 * Perform a looser typed flatMap / bind operation
	 * The return type can be another type other than the host type
	 * 
	 * @param fn flatMap function
	 * @return flatMapped monad
	*/
	 <R> AnyM<R> bind(Function<? super T,?> fn);
	/**
	 * Perform a bind operation (@see #bind) but also lift the return value into a Monad using configured
	 * MonadicConverters
	 * 
	 * @param fn flatMap function
	 * @return flatMapped monad
	 */
	 <R> AnyM<R> liftAndBind(Function<? super T,?> fn);
	/**
	 * Perform a flatMap operation where the result will be a flattened stream of Characters
	 * from the CharSequence returned by the supplied function.
	 * 
	 * <pre>
	 * {@code
	 * List<Character> result = anyM("input.file")
								.liftAndBindCharSequence(i->"hello world")
								.asSequence()
								.toList();
		
		assertThat(result,equalTo(Arrays.asList('h','e','l','l','o',' ','w','o','r','l','d')));
	 * 
	 * }</pre>
	 * 
	 * 
	 * 
	 * @param fn
	 * @return
	 */
	  AnyM<Character> flatMapCharSequence(Function<? super T,CharSequence> fn);
	/**
	 *  Perform a flatMap operation where the result will be a flattened stream of Strings
	 * from the text loaded from the supplied files.
	 * 
	 * <pre>
	 * {@code
	 * 		List<String> result = anyM("input.file")
								.map(getClass().getClassLoader()::getResource)
								.peek(System.out::println)
								.map(URL::getFile)
								.liftAndBindFile(File::new)
								.asSequence()
								.toList();
		
		assertThat(result,equalTo(Arrays.asList("hello","world")));
	 * 
	 * }
	 * 
	 * </pre>
	 * 
	 * @param fn
	 * @return
	 */
	  AnyM<String> flatMapFile(Function<? super T,File> fn);
	/**
	 *  Perform a flatMap operation where the result will be a flattened stream of Strings
	 * from the text loaded from the supplied URLs 
	 * <pre>
	 * {@code 
	 * List<String> result = anyM("input.file")
								.liftAndBindURL(getClass().getClassLoader()::getResource)
								.asSequence()
								.toList();
		
		assertThat(result,equalTo(Arrays.asList("hello","world")));
	 * 
	 * }
	 * 
	 * </pre>
	 * 
	 * 
	 * @param fn
	 * @return
	 */
	  AnyM<String> flatMapURL(Function<? super T, URL> fn) ;
	/**
	  *  Perform a flatMap operation where the result will be a flattened stream of Strings
	 * from the text loaded from the supplied BufferedReaders
	 * 
	 * <pre>
	 * {@code
	 * List<String> result = anyM("input.file")
								.map(getClass().getClassLoader()::getResourceAsStream)
								.map(InputStreamReader::new)
								.liftAndBindBufferedReader(BufferedReader::new)
								.asSequence()
								.toList();
		
		assertThat(result,equalTo(Arrays.asList("hello","world")));
	 * 
	 * }
	 * 
	 * 
	 * 
	 * @param fn
	 * @return
	 */
	  AnyM<String> flatMapBufferedReader(Function<? super T,BufferedReader> fn) ;
	
	/**
	 * join / flatten one level of a nested hierarchy
	 * 
	 * @return Flattened / joined one level
	 */
	 <T1> AnyM<T1> flatten();
	
	/**
	 * Aggregate the contents of this Monad and the supplied Monad 
	 * 
	 * <pre>{@code 
	 * 
	 * List<Integer> result = anyM(Stream.of(1,2,3,4))
	 * 							.aggregate(anyM(Optional.of(5)))
	 * 							.asSequence()
	 * 							.toList();
		
		assertThat(result,equalTo(Arrays.asList(1,2,3,4,5)));
		}</pre>
	 * 
	 * @param next Monad to aggregate content with
	 * @return Aggregated Monad
	 */
	  AnyM<T> aggregate(AnyM<T> next);
	  <R> AnyM<List<R>> aggregateUntyped(AnyM<?> next);
	
	
	/**
	 * flatMap operation
	 * 
	 * @param fn
	 * @return 
	 */
	 <R> AnyM<R> flatMap(Function<? super T,AnyM<? extends R>> fn) ;
	
	/**
	 * Convenience method to allow method reference support, when flatMap return type is a Stream
	 * 
	 * @param fn
	 * @return
	 */
	 <R> AnyM<R> flatMapStream(Function<? super T,BaseStream<? extends R,?>> fn);
	/**
	 * Convenience method to allow method reference support, when flatMap return type is a Streamable
	 * 
	 * @param fn
	 * @return
	 */
	 <R> AnyM<R> flatMapStreamable(Function<? super T,Streamable<R>> fn) ;
	/**
	 * flatMapping to a Stream will result in the Stream being converted to a List, if the host Monad
	 * type is not a Stream (or Stream like type). (i.e.
	 *  <pre>
	 *  {@code  
	 *   AnyM<Integer> opt = anyM(Optional.of(20));
	 *   Optional<List<Integer>> optionalList = opt.flatMap( i -> anyM(Stream.of(1,2,i))).unwrap();  
	 *   
	 *   //Optional [1,2,20]
	 *  }</pre>
	 *  
	 *  In such cases using Arrays.asList would be more performant
	 *  <pre>
	 *  {@code  
	 *   AnyM<Integer> opt = anyM(Optional.of(20));
	 *   Optional<List<Integer>> optionalList = opt.flatMapCollection( i -> asList(1,2,i))).unwrap();  
	 *   
	 *   //Optional [1,2,20]
	 *  }</pre>
	 * @param fn
	 * @return
	 */
	 <R> AnyM<R> flatMapCollection(Function<? super T,Collection<? extends R>> fn);
	/**
	 * Convenience method to allow method reference support, when flatMap return type is a Optional
	 * 
	 * @param fn
	 * @return
	 */
	 <R> AnyM<R> flatMapOptional(Function<? super T,Optional<? extends R>> fn) ;
	 <R> AnyM<R> flatMapCompletableFuture(Function<? super T,CompletableFuture<? extends R>> fn);

	 <R> AnyM<R> flatMapSequenceM(Function<? super T,SequenceM<? extends R>> fn);
	
	
	
	
	/**
	 * Sequence the contents of a Monad.  e.g.
	 * Turn an <pre>
	 * 	{@code Optional<List<Integer>>  into Stream<Integer> }</pre>
	 * 
	 * <pre>{@code
	 * List<Integer> list = anyM(Optional.of(Arrays.asList(1,2,3,4,5,6)))
											.<Integer>toSequence(c->c.stream())
											.collect(Collectors.toList());
		
		
		assertThat(list,hasItems(1,2,3,4,5,6));
		
	 * 
	 * }</pre>
	 * 
	 * @return A Sequence that wraps a Stream
	 */
	 <NT> SequenceM<NT> toSequence(Function<T,Stream<NT>> fn);
	/**
	 *  <pre>{@code Optional<List<Integer>>  into Stream<Integer> }</pre>
	 * Less type safe equivalent, but may be more accessible than toSequence(fn) i.e. 
	 * <pre>
	 * {@code 
	 *    toSequence(Function<T,Stream<NT>> fn)
	 *   }
	 *   </pre>
	 *  <pre>{@code
	 * List<Integer> list = anyM(Optional.of(Arrays.asList(1,2,3,4,5,6)))
											.<Integer>toSequence()
											.collect(Collectors.toList());
		
		
		
	 * 
	 * }</pre>
	
	 * @return A Sequence that wraps a Stream
	 */
	 <T> SequenceM<T> toSequence();
	
	
	/**
	 * Wrap this Monad's contents as a Sequence without disaggreating it. .e.
	 *  <pre>{@code Optional<List<Integer>>  into Stream<List<Integer>> }</pre>
	 * If the underlying monad is a Stream it is returned
	 * Otherwise we flatMap the underlying monad to a Stream type
	 */
	 SequenceM<T> asSequence();
	
	
		
	

	/**
	 * Apply function/s inside supplied Monad to data in current Monad
	 * 
	 * e.g. with Streams
	 * <pre>{@code 
	 * 
	 * AnyM<Integer> applied =anyM(Stream.of(1,2,3)).applyM(anyM(Streamable.of( (Integer a)->a+1 ,(Integer a) -> a*2))).simplex();
	
	 	assertThat(applied.toList(),equalTo(Arrays.asList(2, 2, 3, 4, 4, 6)));
	 }</pre>
	 * 
	 * with Optionals 
	 * <pre>{@code
	 * 
	 *  Any<Integer> applied =anyM(Optional.of(2)).applyM(anyM(Optional.of( (Integer a)->a+1)) );
		assertThat(applied.toList(),equalTo(Arrays.asList(3)));}</pre>
	 * 
	 * @param fn
	 * @return
	 */
	 <R> AnyM<R> applyM(AnyM<Function<? super T,? extends R>> fn);
	/**
	 * Filter current monad by each element in supplied Monad
	 * 
	 * e.g.
	 * 
	 * <pre>{@code
	 *  AnyM<AnyM<Integer>> applied = anyM(Stream.of(1,2,3))
	 *    									.filterM(anyM(Streamable.of( (Integer a)->a>5 ,(Integer a) -> a<3)))
	 *    									.simplex();
	 * 
	 *  //results in AnyM((AnyM(1),AnyM(2),AnyM(())
	 * //or in terms of the underlying monad as Stream.of(Stream.of(1),Stream.of(2),Stream.of(())
	 * }</pre>
	 * 
	 * @param fn
	 * @return
	 */
	   AnyM<AnyM<T>> simpleFilter(AnyM<Predicate<? super T>> fn);
	   AnyM<Stream<T>> simpleFilter(Stream<Predicate<? super T>> fn);
	   AnyM<Stream<T>> simpleFilter(Streamable<Predicate<? super T>> fn);
	   AnyM<Optional<T>> simpleFilter(Optional<Predicate<? super T>> fn);
	   AnyM<CompletableFuture<T>> simpleFilter(CompletableFuture<Predicate<? super T>> fn);
	  
	public <T> AnyM<T> unit(T value);
	public <T> AnyM<T> empty();
	/**
	 * 
	 * Replicate given Monad
	 * 
	 * <pre>{@code 
	 * 	
	 *   AnyM<Optional<Integer>> applied =monad(Optional.of(2)).replicateM(5).simplex();
		 assertThat(applied.unwrap(),equalTo(Optional.of(Arrays.asList(2,2,2,2,2))));
		 
		 }</pre>
	 * 
	 * 
	 * @param times number of times to replicate
	 * @return Replicated Monad
	 */
	 AnyM<List<T>> replicateM(int times);
	/**
	 * Perform a reduction where NT is a (native) Monad type
	 * e.g. 
	 * <pre>{@code 
	 * Monoid<Optional<Integer>> optionalAdd = Monoid.of(Optional.of(0), (a,b)-> Optional.of(a.get()+b.get()));
		
		assertThat(monad(Stream.of(2,8,3,1)).reduceM(optionalAdd).unwrap(),equalTo(Optional.of(14)));
		}</pre>
	 * 
	 * 
	 * @param reducer
	 * @return
	 */
	 AnyM<T> reduceMOptional(Monoid<Optional<T>> reducer);
	 AnyM<T> reduceMStream(Monoid<Stream<T>> reducer);
	 AnyM<T> reduceMStreamable(Monoid<Streamable<T>> reducer);
	 AnyM<T> reduceMCompletableFuture(Monoid<CompletableFuture<T>> reducer);
	  
	 AnyM<T> reduceM(Monoid<AnyM<T>> reducer);
	
	
	 
	
	@Override
    public String toString() ;
	default Optional<List<T>> toOptional(){
		
		return this.<T>toSequence().toOptional();
	}
	default CompletableFuture<List<T>> toCompletableFuture(){
		return this.<T>toSequence().toCompletableFuture();
	}
	
	/**
	 * Convert this monad into a List
	 * <pre>
	 * @{code 
	 * 
	 * Stream<Integer> becomes List<Integer>
	 * Optional<Integer> becomes List<Integer>
	 * Set<Integer> becomes List<Integer>
	 * }
	 * </pre>
	 * 
	 * @return AnyM as a List
	 */
	public List<T> toList();
	/**
	 * Convert this monad into a Set
	 * <pre>
	 * @{code 
	 * 
	 * Stream<Integer> becomes Set<Integer>
	 * Optional<Integer> becomes Set<Integer>
	 * List<Integer> becomes Set<Integer>
	 * 
	 * }
	 * </pre>
	 * 
	 * @return AnyM as a Set
	 */
	public Set<T> toSet();
	
	/**
	 * Collect the contents of the monad wrapped by this AnyM into supplied collector
	 */
	public <R, A> R collect(Collector<? super T, A, R> collector);
	
	 
	
}
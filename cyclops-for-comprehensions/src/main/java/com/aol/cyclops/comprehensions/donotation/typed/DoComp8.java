package com.aol.cyclops.comprehensions.donotation.typed;

import java.util.function.Function;

import org.pcollections.PStack;

import com.aol.cyclops.lambda.api.AsAnyM;
import com.aol.cyclops.lambda.monads.MonadWrapper;
import com.aol.cyclops.monad.AnyM;


public class DoComp8<T,T1,T2,T3,T4,T5,T6,T7> extends DoComp{
	public DoComp8(PStack<Entry> assigned, Class orgType) {
		super(assigned,orgType);
		
	}
	public  DoComp8<T,T1,T2,T3,T4,T5,T6,T7> filter(Function<T,Function<T1,Function<T2,Function<T3,Function<T4,Function<T5,Function<T6,Function<T7,Boolean>>>>>>>> f){
		return new DoComp8(getAssigned().plus(getAssigned().size(),new Entry("$$internalGUARD"+getAssigned().size(),new Guard(f))),getOrgType());
	}
	public <R> AnyM<R> yield(Function<T,Function<T1,Function<T2,Function<T3,Function<T4,Function<T5,Function<T6,Function<T7,R>>>>>>>>  f){
		if(getOrgType()!=null)
			return new MonadWrapper(this.yieldInternal(f),this.getOrgType()).anyM();
		else
			return AnyM.ofMonad(this.yieldInternal(f));
	}
}
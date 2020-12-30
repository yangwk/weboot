package com.github.yangwk.weboot.common.lock;

import java.util.function.Function;

public interface DistributedLockAction {

	<T, R> R doInLock(String lockName, T t, Function<T, R> function, long waitTime, long leaseTime);

}

/*
 * Copyright (c) 2014, Victor Nazarov <asviraspossible@gmail.com>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 *
 *  1. Redistributions of source code must retain the above copyright notice,
 *     this list of conditions and the following disclaimer.
 *
 *  2. Redistributions in binary form must reproduce the above copyright notice,
 *     this list of conditions and the following disclaimer in the documentation and/or
 *     other materials provided with the distribution.
 *
 *  3. Neither the name of the copyright holder nor the names of its contributors
 *     may be used to endorse or promote products derived from this software
 *     without specific prior written permission.
 *
 *  THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 *  ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 *  THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 *  IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR
 *  ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 *  (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 *   LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 *  ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 *  (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 *  EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.github.sviperll.adt4j;

import com.github.sviperll.meta.MemberAccess;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Generates predicate for specified case
 * <p>
 * Predicate is used to test any ADT-value to find out if it represents some specific case.
 * <p>
 * For example, <tt>isEmpty</tt> predicate can be used to test if given single-linked list is empty.
 *
 * @author Victor Nazarov <asviraspossible@gmail.com>
 */
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.METHOD)
@Documented
public @interface GeneratePredicate {
    /**
     * Name of generated predicate.
     * <p>
     * Name argument for GeneratePredicate annotation can be omitted.
     * In such case visitor-interface method name with <tt>is</tt> prefix will be used.
     * <p>
     * <blockquote><pre><code>
     *     interface ListVisitor&lt;L, T, R&gt; {
     *
     *         &#64;GeneratePredicate
     *         R empty();
     *
     *         R prepend(T head, L tail);
     *     }
     * </code></pre></blockquote>
     * <p>
     * <tt>isEmpty</tt> predicate will be generated in the example above.
     * 
     * @return Name of generated predicate
     */
    String name() default ":auto";

    /**
     * Java's access modifier for generated predicate.
     *
     * @return Java's access modifier for generated predicate.
     */
    MemberAccess access() default MemberAccess.PUBLIC;
}

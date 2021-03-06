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
import com.github.sviperll.Caching;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Generates value class for annotated visitor-interface.
 *
 * @see <a href="https://github.com/sviperll/adt4j">ADT4J project page</a>
 *
 * @author Victor Nazarov <asviraspossible@gmail.com>
 */
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.TYPE)
@Documented
public @interface GenerateValueClassForVisitor {
    /**
     * Name of generated accept-method.
     * <p>
     * Accept method perform actual "visit".
     *
     * @return Name of generated accept-method
     */
    String acceptMethodName() default "accept";

    /**
     * Java's access modifier for generated accept-method.
     * <p>
     * Accept method perform actual "visit".
     * <p>
     * Default is public.
     * 
     * @return Java's access modifier for generated accept-method.
     */
    MemberAccess acceptMethodAccess() default MemberAccess.PUBLIC;

    /**
     * Name of generated class (algebraic data type).
     * <p>
     * You can leave className parameter out.
     * Generated class name is chosen by removing "Visitor"-suffix from visitor-interface name.
     * If visitor-interface name doesn't end with "Visitor",
     * "Value"-suffix is appended to visitor-interface name to form generated class name.
     *
     * @return Name of generated class (algebraic data type).
     */
    String className() default ":auto";

    /**
     * Name of an interface for value classes to implement.
     * <p>
     * You can leave the baseInterface parameter out.
     * Generated classes will implement this interface, allowing the developer to work with abstractions,
     * and make use of Java 8 default methods.
     * @return Class name of marker interface.
     */
    String baseInterface() default "";

    /**
     * Name of an class for value classes to extend.
     * <p>
     * You can leave the baseClass parameter out.
     * Generated classes will extends this interface, allowing the developer to work with abstractions,
     * and make use of Java 8 default methods.
     * @return Class name of marker interface.
     */
    String baseClass() default "";

    /**
     * Java's access for generated class.
     * <p>
     * Public or package-private.
     * <p>
     * Default name is package-private
     * 
     * @return Java's access for generated class.
     */
    boolean isPublic() default false;

    /**
     * Base integer to perform hash code evaluation in generated class.
     * <p>
     * Should be prime for better results.
     *
     * @return Base integer to perform hash code evaluation in generated class.
     */
    int hashCodeBase() default 37;

    /**
     * hashCode method caching strategy.
     * <p>
     * You should probably always use {@code NONE} caching,
     * until you actually need some hashCode speedup.
     * <p>
     * The only case when strategy can be anything but {@code NONE} is
     * when all fields are actually immutable.
     * <p>
     * {@code PRECOMPUTE} strategy can potentially speed up equals method
     *
     * @see Caching
     */
    Caching hashCodeCaching() default Caching.NONE;

    /**
     * Specifies weather generated class should be serializable.
     *
     * @return weather generated class should be serializable.
     */
    boolean isSerializable() default false;

    /**
     * Specifies weather generated class should be comparable.
     * <p>
     * Comparable class implements java.lang.Comparable interface.
     *
     * @return weather generated class should be comparable.
     */
    boolean isComparable() default false;

    /**
     * serialVersionUID value for generated serializable class.
     * <p>
     * Serializable classes should provide serialVersionUID field for stable serialization
     *
     * @return serialVersionUID value for generated serializable class.
     */
    long serialVersionUID() default 1L;
}

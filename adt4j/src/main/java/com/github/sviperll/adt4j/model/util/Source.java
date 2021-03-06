/*
 * Copyright (c) 2015, Victor Nazarov <asviraspossible@gmail.com>
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
package com.github.sviperll.adt4j.model.util;

import com.github.sviperll.meta.MemberAccess;
import com.github.sviperll.meta.SourceCodeValidationException;
import com.helger.jcodemodel.AbstractJType;
import com.helger.jcodemodel.IJExpression;
import com.helger.jcodemodel.IJStatement;
import com.helger.jcodemodel.JAnnotationStringValue;
import com.helger.jcodemodel.JAnnotationUse;
import com.helger.jcodemodel.JBlock;
import com.helger.jcodemodel.JFieldRef;
import com.helger.jcodemodel.JFormatter;
import com.helger.jcodemodel.JMod;
import com.helger.jcodemodel.JTypeWildcard;
import com.helger.jcodemodel.JVar;
import java.text.MessageFormat;
import java.util.Locale;
import java.util.NoSuchElementException;

/**
 *
 * @author Victor Nazarov <asviraspossible@gmail.com>
 */
public class Source {
    @SuppressWarnings("unchecked")
    public static <T> T getAnnotationArgument(JAnnotationUse annotation, String name, Class<T> klass) {
        JAnnotationStringValue stringValue = annotation.getConstantParam(name);
        if (stringValue != null)
            return (T)stringValue.nativeValue();
        else {
            throw new NoSuchElementException(MessageFormat.format("{0} annotation argument not found for {1} annotation",
                                                                  name, annotation));
        }
    }

    public static int toJMod(MemberAccess accessLevel) {
        switch (accessLevel) {
            case PRIVATE:
                return JMod.PRIVATE;
            case PACKAGE:
                return JMod.NONE;
            case PROTECTED:
                return JMod.PROTECTED;
            case PUBLIC:
                return JMod.PUBLIC;
            default:
                throw new IllegalStateException("Unsupported AccessLevel: " + accessLevel);
        }
    }

    public static AbstractJType toDeclarable(AbstractJType type) {
        if (type instanceof JTypeWildcard) {
            JTypeWildcard wild = (JTypeWildcard)type;
            return wild.bound();
        }
        return type;
    }

    public static String capitalize(String s) {
        if (s.length() >= 2
            && Character.isHighSurrogate(s.charAt(0))
            && Character.isLowSurrogate(s.charAt(1))) {
            return s.substring(0, 2).toUpperCase(Locale.US) + s.substring(2);
        } else {
            return s.substring(0, 1).toUpperCase(Locale.US) + s.substring(1);
        }
    }

    public static String decapitalize(String s) {
        if (s.length() >= 2
            && Character.isHighSurrogate(s.charAt(0))
            && Character.isLowSurrogate(s.charAt(1))) {
            return s.substring(0, 2).toLowerCase(Locale.US) + s.substring(2);
        } else {
            return s.substring(0, 1).toLowerCase(Locale.US) + s.substring(1);
        }
    }

    public static boolean isNullable(JVar param) throws SourceCodeValidationException {
        boolean hasNonnull = false;
        boolean hasNullable = false;
        for (JAnnotationUse annotationUse: param.annotations()) {
            if (annotationUse.getAnnotationClass().fullName().equals("javax.annotation.Nonnull")) {
                hasNonnull = true;
            }
            if (annotationUse.getAnnotationClass().fullName().equals("javax.annotation.Nullable")) {
                hasNullable = true;
            }
        }
        if (hasNonnull && hasNullable)
            throw new SourceCodeValidationException(MessageFormat.format("Parameter {0} is declared as both @Nullable and @Nonnull",
                                                           param.name()));
        if (!param.type().isReference() && hasNullable)
            throw new SourceCodeValidationException(MessageFormat.format("Parameter {0} is non-reference, but declared as @Nullable",
                                                           param.name()));
        return hasNullable;
    }

    public static JBlock addSynchronizedBlock(JBlock body, final IJExpression lock) {
        /*
         * HACK:
         *  jcodemodel currently provides no support for synchronized blocks.
         *  Implement synchronized block right here:
         */
        final JBlock synchronizedBlock = new JBlock(true, true) {
        };
        body.add(new IJStatement() {
            @Override
            public void state(JFormatter f) {
                f.print("synchronized (").generable(lock).print(") ").generable(synchronizedBlock).newline();
            }
        });
        return synchronizedBlock;
    }

    private Source() {
    }
}

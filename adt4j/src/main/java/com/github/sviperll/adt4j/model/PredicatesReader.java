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
package com.github.sviperll.adt4j.model;

import com.github.sviperll.adt4j.GeneratePredicate;
import com.github.sviperll.adt4j.GeneratePredicates;
import com.github.sviperll.adt4j.model.util.Source;
import com.github.sviperll.meta.MemberAccess;
import com.github.sviperll.meta.SourceCodeValidationException;
import com.helger.jcodemodel.JAnnotationArrayMember;
import com.helger.jcodemodel.JAnnotationUse;
import com.helger.jcodemodel.JMethod;
import java.text.MessageFormat;
import java.util.Collection;
import java.util.Map;

/**
 *
 * @author Victor Nazarov <asviraspossible@gmail.com>
 */
class PredicatesReader {
    private final Map<String, PredicateConfigutation> predicates;

    public PredicatesReader(Map<String, PredicateConfigutation> predicates) {
        this.predicates = predicates;
    }

    private void read(JMethod interfaceMethod, String predicateName, MemberAccess accessLevel) throws SourceCodeValidationException {
        if (predicateName.equals(":auto")) {
            predicateName = "is" + Source.capitalize(interfaceMethod.name());
        }
        PredicateConfigutation existingConfiguration = predicates.get(predicateName);
        if (existingConfiguration == null) {
            existingConfiguration = new PredicateConfigutation(interfaceMethod, accessLevel);
            predicates.put(predicateName, existingConfiguration);
        }
        try {
            existingConfiguration.put(interfaceMethod, accessLevel);
        } catch (PredicateConfigurationException ex) {
            throw new SourceCodeValidationException(MessageFormat.format("Unable to generate {0} predicate: inconsistent access levels",
                                                           predicateName), ex);
        }
    }

    void read(JMethod interfaceMethod, JAnnotationUse annotationUsage) throws SourceCodeValidationException {
        String annotationClassName = annotationUsage.getAnnotationClass().fullName();
        if (annotationClassName != null) {
            if (annotationClassName.equals(GeneratePredicate.class.getName())) {
                String predicateName = Source.getAnnotationArgument(annotationUsage, "name", String.class);
                MemberAccess accessLevel = Source.getAnnotationArgument(annotationUsage, "access", MemberAccess.class);
                read(interfaceMethod, predicateName, accessLevel);
            } else if (annotationClassName.equals(GeneratePredicates.class.getName())) {
                JAnnotationArrayMember values = (JAnnotationArrayMember)annotationUsage.getParam("value");
                if (values != null && values.size() > 0) {
                    Collection<JAnnotationUse> annotations = values.annotations();
                    for (JAnnotationUse annotation: annotations) {
                        read(interfaceMethod, annotation);
                    }
                }
            }
        }
    }

}

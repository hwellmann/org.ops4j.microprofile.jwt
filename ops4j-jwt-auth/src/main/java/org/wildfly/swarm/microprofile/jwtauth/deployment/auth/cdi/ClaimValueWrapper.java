/**
 *
 *   Copyright 2017 Red Hat, Inc, and individual contributors.
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 *
 */
package org.wildfly.swarm.microprofile.jwtauth.deployment.auth.cdi;

import org.eclipse.microprofile.jwt.ClaimValue;

/**
 * An implementation of the ClaimValue interface
 *
 * @param <T> the claim value type
 */
public class ClaimValueWrapper<T> implements ClaimValue<T> {
    private String name;

    private T value;

    public ClaimValueWrapper(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return String.format("ClaimValueWrapper[@%s], name=%s, value[%s]=%s", Integer.toHexString(hashCode()),
                name, value.getClass(), value);
    }
}

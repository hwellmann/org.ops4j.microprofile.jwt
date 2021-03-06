/**
 *
 *   Copyright 2018 Red Hat, Inc, and individual contributors.
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
package org.ops4j.microprofile.jwt.authz;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.SecurityContext;
import java.util.HashSet;
import java.util.Set;

import static java.util.Arrays.asList;

/**
 * @author Michal Szynkiewicz, michal.l.szynkiewicz@gmail.com
 * <br>
 * Date: 6/12/18
 */
@Priority(Priorities.AUTHORIZATION)
public class RolesAllowedFilter implements ContainerRequestFilter {

    private final Set<String> allowedRoles;
    private final boolean allRolesAllowed;

    public RolesAllowedFilter(String[] allowedRoles) {
        this.allowedRoles = new HashSet<>(asList(allowedRoles));
        this.allRolesAllowed = this.allowedRoles.stream().anyMatch("*"::equals);
    }

    @Override
    public void filter(ContainerRequestContext requestContext) {
        SecurityContext securityContext = requestContext.getSecurityContext();
        boolean isForbidden;
        if (allRolesAllowed) {
            isForbidden = securityContext.getUserPrincipal() == null;
        } else {
            isForbidden = allowedRoles.stream().noneMatch(securityContext::isUserInRole);
        }
        if (isForbidden) {
            MPJwtRequestFailer.fail(requestContext);
        }
    }
}

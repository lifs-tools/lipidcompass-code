/* 
 * Copyright 2021 The LipidCompass Developers.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.lipidcompass.data.model;

/**
 * Visibility modifiers for {@link ArangoBaseEntity}. An object with visibility
 * PRIVATE can only be seen by its owner, curators and administrators. With
 * visibility RESTRICTED, the object will be visible, if accessed via a
 * specific link. With visibility PUBLIC, the object will be publically
 * browseable and searchable. It will also be indexed and cross linked to the
 * lipid identities known in Lipid Compass, or new ones will be created, if they
 * did not exist.
 *
 * @author nils.hoffmann
 */
public enum Visibility {
    PRIVATE, RESTRICTED, PUBLIC;
}

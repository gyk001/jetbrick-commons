/**
 * Copyright 2013-2014 Guoqiang Chen, Shanghai, China. All rights reserved.
 *
 *   Author: Guoqiang Chen
 *    Email: subchen@gmail.com
 *   WebURL: https://github.com/subchen
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package jetbrick.io.resource;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import jetbrick.util.*;

public final class ClasspathResource extends AbstractResource {
    private final URL url;
    private final String path;

    public ClasspathResource(String path) {
        this(path, null);
    }

    public ClasspathResource(String path, ClassLoader loader) {
        Validate.notNull(path);

        if (loader == null) {
            loader = ClassLoaderUtils.getDefault();
        }
        path = StringUtils.removeStart(path, "/");

        this.url = loader.getResource(path);
        this.path = path;
        this.relativePathName = path;
    }

    @Override
    public InputStream openStream() throws ResourceNotFoundException {
        if (url == null) {
            throw new ResourceNotFoundException(path);
        }
        try {
            return url.openStream();
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public URL getURL() {
        return url;
    }

    @Override
    public boolean exist() {
        return url != null;
    }

    @Override
    public boolean isDirectory() {
        return path.endsWith("/");
    }

    @Override
    public boolean isFile() {
        return !path.endsWith("/");
    }

    @Override
    public long length() {
        if (url == null) {
            return -1;
        }
        try {
            return url.openConnection().getContentLengthLong();
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public long lastModified() {
        if (url == null) {
            return 0;
        }
        try {
            return url.openConnection().getLastModified();
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public String toString() {
        return URL_PREFIX_CLASSPATH + path;
    }
}

# Arbalest

Yet another Parse REST client for Android.

## Getting started

TODO

## How to use

1. Initialize ParseRest object on Application#onCreate()
```Java
package arbalest.sample;

import android.app.Application;

import arbalest.Arbalest;

public class ArbalestApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Arbalest.initialize(this, "your application id here", "your rest key here");
    }
}
```

TODO

## License

```
Copyright (C) 2013 KeithYokoma. All rights reserved.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing,
software distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```

# soundcloud

FIXME: description

## Installation

Download from http://example.com/FIXME.

## Usage

Collect and aggregate user data from Soundcloud into a database using Clojure and PostgreSQL.

Soundcloud seem to have an open API. If you look at the requests on a soundcloud page you will see requests being made with a client_id. Use this client ID on your config here.

1. Create a PostgreSQL database called `soundcloud_stats`
2. In `soundcloud_stats` run the SQL script `database/schema_migrations/12.27.2020.11.55_users_and_tracks_up.sql` to create the database tables.
3. Create a `config.edn` file and store it in `src/soundcloud/config.edn` with the following format
    
    ```
    config.edn 
    # ----------------
    {
        :client-id "ABCDEF"
        :users [{
                  :username "some-user"
                  :user-id "1234"
                 } {
                  :username "another-user"
                  :user-id "5678"}
                ...]
	}
    ```
4. Run `-main` and all user and user tracks information will be scraped and stored in the database.

FIXME: explanation

    $ java -jar soundcloud-0.1.0-standalone.jar [args]

## Options

FIXME: listing of options this app accepts.

## Examples

...

### Bugs

...

### Any Other Sections
### That You Think
### Might be Useful

## License

Copyright © 2020 FIXME

This program and the accompanying materials are made available under the
terms of the Eclipse Public License 2.0 which is available at
http://www.eclipse.org/legal/epl-2.0.

This Source Code may also be made available under the following Secondary
Licenses when the conditions for such availability set forth in the Eclipse
Public License, v. 2.0 are satisfied: GNU General Public License as published by
the Free Software Foundation, either version 2 of the License, or (at your
option) any later version, with the GNU Classpath Exception which is available
at https://www.gnu.org/software/classpath/license.html.

# ZK component Aggrid

## Prerequisite
* Java 8
* Maven 3

# Run test app
```
mvn jetty:run
```

Then open http://localhost:8080 in any browser.

# JavaScript/TypeScript linting
* Need `npm` or `yarn`
```
npm run lint
```
```
yarn lint
```

# Watch changes and start livereload server
* Need `npm` or `yarn`
* Need to "Run test app" first
```
npm run dev
```
```
yarn dev
```

Once TypeScript/JavaScript/LESS files being changed, the compiled files will be updated. The browser will be reloaded automatically to see the changes. It is convenient when developing a widget.

The default livereload proxy: http://localhost:3000/

# Make bundle package

```
mvn package
```

The result will be in the `target` folder.
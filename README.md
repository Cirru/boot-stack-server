
Stack Editor Server
----

...for https://github.com/Cirru/stack-editor

> The npm package is `stack-editor` despite the repo is `stack-server`.

### Usage

```bash
npm i -g stack-editor
stack-editor
se # for short
```

configure it with:

```bash
op=watch port=7010 extension=.cljs out=src stack-editor ir.edn
```

`op` for operation: `watch` or `compile`.

`ir.edn` is the default filename.

### Develop

```bash
yarn
yarn cljs # generate cljs in compiled/
node entry/bin.js
```

```bash
yarn cljs-once
webpack # bundle code to dist/
```

### License

MIT

var gulp = require('gulp');
var minimist = require('minimist');
var babel = require('gulp-babel');
var sourcemaps = require('gulp-sourcemaps');
var rename = require('gulp-rename');
var uglify = require('gulp-uglify');
var less = require('gulp-less');
var cleanCSS = require('gulp-clean-css');
var browserSync = require('browser-sync').create();

var knownOptions = {
    string: ['src', 'dest'],
    default: {
        src: 'src/main/resources',
        dest: 'target/classes'
    }
};
var options = minimist(process.argv.slice(2), knownOptions);
var srcBase = stripQuotes(options.src);
var destBase = stripQuotes(options.dest);

// Workaround for maven frontend-maven-plugin passing quoted strings
function stripQuotes(txt) {
    if (txt.charAt(0) === '"' && txt.charAt(txt.length - 1) === '"') {
        return txt.substring(1, txt.length - 1);
    }
    return txt;
}

function watch_job(glob, job) {
    var watcher = gulp.watch(glob, {ignoreInitial: false}, job);
    watcher.on('change', function (path) {
        // eslint-disable-next-line no-console
        console.log('Detect file change: ' + path + '...');
    });
    return watcher;
}

function typescript_production() {
    var sources = srcBase + '/**/*.ts';
    return gulp.src(sources)
        .pipe(sourcemaps.init())
        .pipe(babel())
        .pipe(rename({suffix: '.src'}))
        .pipe(gulp.dest(destBase))
        .pipe(uglify())
        .pipe(rename(function (path) {
            path.basename = path.basename.replace(/\.src/, '');
        }))
        .pipe(sourcemaps.write('.', {addComment: false, includeContent: false}))
        .pipe(gulp.dest(destBase));
}

function browsersync_init(done) {
    browserSync.init({
        proxy: 'localhost:8080'
    });
    done();
}

function watch_typescript() {
    return watch_job(srcBase + '/**/*.ts', typescript_dev);
}

function typescript_dev() {
    return gulp.src(srcBase + '/**/*.ts', {since: gulp.lastRun(typescript_dev)})
        .pipe(babel())
        .pipe(gulp.dest(destBase))
        .pipe(rename({suffix: '.src'}))
        .pipe(gulp.dest(destBase))
        .pipe(browserSync.stream());
}

function stylesheet_production() {
    return gulp.src(srcBase + '/**/less/*.less')
        .pipe(less())
        .pipe(rename(function (path) {
            path.dirname = path.dirname.replace(/\/less$/, '/css');
            path.extname = '.css.dsp.src';
        }))
        .pipe(gulp.dest(destBase))
        .pipe(cleanCSS())
        .pipe(rename(function (path) {
            path.extname = path.extname.replace(/\.src$/, '');
        }))
        .pipe(gulp.dest(destBase));
}

function watch_stylesheet() {
    return watch_job(srcBase + '/**/less/*.less', stylesheet_dev);
}

function stylesheet_dev() {
    return gulp.src(srcBase + '/**/less/*.less')
        .pipe(less())
        .pipe(rename(function (path) {
            path.dirname = path.dirname.replace(/\/less$/, '/css');
            path.extname = '.css.dsp';
        }))
        .pipe(gulp.dest(destBase))
        .pipe(browserSync.stream());
}

function watch_javascript() {
    return watch_job(srcBase + '/**/*.js', javascript_dev);
}

function javascript_dev() {
    return gulp.src(srcBase + '/**/*.js')
        .pipe(gulp.dest(destBase))
        .pipe(rename({suffix: '.src'}))
        .pipe(gulp.dest(destBase))
        .pipe(browserSync.stream());
}

exports.watch = gulp.series(browsersync_init, gulp.parallel(watch_typescript, watch_stylesheet, watch_javascript));
exports.build = gulp.parallel(typescript_production, stylesheet_production);
exports.default = exports.build;

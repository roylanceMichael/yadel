
var gulp = require('gulp');
var gulpsync = require('gulp-sync')(gulp);
var sass = require('gulp-sass');
var concat = require('gulp-concat');
var del = require('del');
var uglify = require('gulp-uglify');
var rename = require('gulp-rename');
var webpack = require('webpack-stream');
var ts = require('gulp-tsc');

// Clean
gulp.task('clean', function() {
    const delConfig = ['dest', '../webapp/dist'];
    return del(delConfig, { force: true});
});

// Clean Dest
gulp.task('clean:dest', function() {
    const delConfig = ['dest'];
    return del(delConfig, { force: true});
});

// Compile Css
gulp.task('css', function() {
    return gulp.src(['./node_modules/bootstrap/dist/css/bootstrap.css',
        './node_modules/bootstrap/dist/css/bootstrap-theme.css',
        './node_modules/font-awesome/css/font-awesome.css',
        './css/site.css'])
        .pipe(concat('all.css'))
        .pipe(gulp.dest('../webapp/dist'));
});

// Concatenate & Minify JS
gulp.task('scripts:vendor', function() {
    return gulp.src([
        './node_modules/long/dist/long.js',
        './node_modules/bytebuffer/dist/bytebuffer.js',
        './node_modules/protobufjs/dist/protobuf.js',
        './js/*.js',
        './node_modules/jquery/dist/jquery.js',
        './node_modules/bootstrap/dist/js/bootstrap.js',
        './node_modules/angular/angular.js',
        './node_modules/angular-route/angular-route.js',
        './node_modules/d3/d3.js',
        './node_modules/dagre-d3/dist/dagre-d3.js'])
        .pipe(concat('vendor.js'))
        .pipe(rename('vendor.min.js'))
        .pipe(gulp.dest('../webapp/dist'));
});

gulp.task('scripts:typescript', function() {
    return gulp.src(['app/**/*.ts', './node_modules/org.roylance.yadel.api/*.ts'])
        .pipe(ts())
        .pipe(gulp.dest("dest"));
});

gulp.task('scripts', function() {
    return gulp.src(['dest/**/*'])
        .pipe(webpack())
        .pipe(concat('all.js'))
        .pipe(rename('all.min.js'))
        .pipe(gulp.dest('../webapp/dist'));
});

// default (no minify)
gulp.task('default', gulpsync.sync([
    'clean',
    'css',
    'scripts:vendor',
    'scripts:typescript',
    'scripts',
    'clean:dest'
]));

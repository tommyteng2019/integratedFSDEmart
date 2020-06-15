# stage 1
FROM node:alpine AS my-app-build
WORKDIR /app
COPY . .
RUN npm install && npm run build

# stage 2
FROM nginx:1.13.3-alpine
COPY /nginx/default.conf /etc/nginx/conf.d/
RUN rm -rf /usr/share/nginx/html/*
COPY dist/emart /usr/share/nginx/html
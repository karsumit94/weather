FROM node:16 AS builder
WORKDIR /app
COPY . .
RUN yarn install && yarn build

FROM nginx:alpine
WORKDIR /usr/share/nginx/html
RUN rm -rf ./*
COPY --from=builder /app/build .
COPY ./config/default.conf /etc/nginx/
ENTRYPOINT ["nginx", "-g", "daemon off;"]
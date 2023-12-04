FROM node:20.10.0-slim
WORKDIR /usr/src/app
COPY package.json .
RUN npm i -g @angular/cli@~10.2.4
RUN npm i
COPY . .
RUN ng build
CMD ng serve --host 0.0.0.0

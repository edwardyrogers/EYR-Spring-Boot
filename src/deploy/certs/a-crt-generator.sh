openssl req \
  -newkey rsa:4096 \
  -x509 \
  -keyout ca.key \
  -out ca.crt \
  -days 365 \
  -nodes \
  -subj "$(cat a-crt-signing-sub.txt)/CN=EYR"

openssl req \
  -newkey rsa:4096 \
  -keyout server.key \
  -out server.csr \
  -nodes \
  -days 365 \
  -config a-openssl-san.cnf \
  -extensions req_ext \
  -subj "$(cat a-crt-signing-sub.txt)/CN=localhost"

openssl x509 \
  -req \
  -in server.csr \
  -out server.crt \
  -CA ca.crt \
  -CAkey ca.key \
  -CAcreateserial \
  -days 365 \
  -extfile a-openssl-san.cnf \
  -extensions req_ext

openssl req \
  -newkey rsa:4096 \
  -keyout client.key \
  -out client.csr \
  -nodes \
  -days 365 \
  -subj "$(cat a-crt-signing-sub.txt)/CN=Client App";

openssl x509 \
  -req \
  -in client.csr \
  -out client.crt \
  -CA ca.crt \
  -CAkey ca.key \
  -CAcreateserial \
  -days 365

openssl pkcs12 \
  -export \
  -out client.p12 \
  -inkey client.key \
  -in client.crt \
  -certfile ca.crt
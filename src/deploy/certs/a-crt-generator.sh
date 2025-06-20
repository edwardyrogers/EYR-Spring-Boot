#!/bin/bash

# Fail script on any error
set -e

# Common Subject DN prefix
SUBJECT_PREFIX="/C=TW/ST=Taipei/L=Taipei City/O=EYR Company/OU=Idea Department"

# Generate CA certificate
openssl req \
  -newkey rsa:4096 \
  -x509 \
  -keyout rot.key \
  -out rot.crt \
  -days 365 \
  -nodes \
  -subj "${SUBJECT_PREFIX}/CN=EYR"


openssl genrsa \
  -out imt.key 4096

openssl req \
  -new \
  -key imt.key \
  -out imt.csr \
  -subj "$(cat a-crt-signing-sub.txt)/CN=EYR Intermediate CA"

cat > imt.ext <<EOF
basicConstraints=CA:TRUE,pathlen:0
keyUsage = critical, cRLSign, keyCertSign
subjectKeyIdentifier=hash
authorityKeyIdentifier=keyid:always,issuer
EOF

openssl x509 \
  -req \
  -in imt.csr \
  -CA rot.crt \
  -CAkey rot.key \
  -CAcreateserial \
  -out imt.crt \
  -days 365 \
  -sha256 \
  -extfile imt.ext


# Generate Server key and CSR
openssl req \
  -newkey rsa:4096 \
  -keyout srv.key \
  -out srv.csr \
  -nodes \
  -config a-openssl-san.cnf \
  -extensions req_ext \
  -subj "${SUBJECT_PREFIX}/CN=localhost"

# Sign Server CSR with CA
openssl x509 \
  -req \
  -in srv.csr \
  -out srv.crt \
  -CA imt.crt \
  -CAkey imt.key \
  -CAcreateserial \
  -days 365 \
  -extfile a-openssl-san.cnf \
  -extensions req_ext

# Generate Client key and CSR
openssl req \
  -newkey rsa:4096 \
  -keyout cnt.key \
  -out cnt.csr \
  -nodes \
  -subj "${SUBJECT_PREFIX}/CN=Client App"

# Sign Client CSR with CA
openssl x509 \
  -req \
  -in cnt.csr \
  -out cnt.crt \
  -CA imt.crt \
  -CAkey imt.key \
  -CAcreateserial \
  -days 365


cat srv.crt imt.crt > srv-chain.crt

cat imt.crt rot.crt > rot-chain.crt


## Create PKCS#12 bundle for Client
#openssl pkcs12 \
#  -export \
#  -out client.p12 \
#  -inkey client.key \
#  -in client.crt \
#  -certfile rot-chain.crt \
#  -passout Password  # <-- adjust if you want a password

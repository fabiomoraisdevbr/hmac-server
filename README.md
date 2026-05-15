<div align="center">
<h1>servidor com autenticação via HMAC</h1>


![Java](https://img.shields.io/badge/Java_25-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white)
![Docker](https://img.shields.io/badge/Docker-2496ED?style=for-the-badge&logo=docker&logoColor=white)
![HMAC](https://img.shields.io/badge/HMAC--SHA512-FF6B6B?style=for-the-badge&logo=security&logoColor=white)

</div>

---

## 📌 Sobre o Projeto

API REST desenvolvida como trabalho da disciplina de **Segurança e Auditoria de Sistemas** (TADS – IFPR), que simula uma **estação meteorológica** capaz de enviar dados climáticos de forma segura para um servidor central.

A autenticidade e integridade das mensagens são garantidas através de **HMAC-SHA512** — um código de autenticação baseado em hash que impede adulteração dos dados em trânsito.

---

## ⚙️ Funcionalidades

| Funcionalidade | Status |
|---|---|
| Geração de mensagem autenticada com HMAC-SHA512 | ✅ Implementado |
| Validação de mensagem no servidor | ✅ Implementado |
| Proteção contra ataques de Replay (timestamp de 15 min) | ✅ Implementado |
| Proteção contra replay com nonce via Redis (cache) | ✅ implementado |

---

## 🔐 Como funciona o HMAC?

O **HMAC (Hash-based Message Authentication Code)** combina a mensagem com uma **chave secreta** para gerar uma assinatura. Qualquer alteração na mensagem — mesmo de um único caractere — produz um hash completamente diferente, tornando a falsificação detectável.

```
HMAC(chave_secreta, mensagem) → hash único
```

Neste projeto, a chave secreta é compartilhada entre a estação e o servidor. O fluxo é:

```
Estação                          Servidor
   |                                |
   |── GET /message ──────────────>|
   |<─ { message, timestamp, hash }─|
   |                                |
   |── POST /message ─────────────>|  valida HMAC + timestamp
   |<─ 200 true / 401 Unauthorized ─|
```

---

## 🛡️ Proteção contra Replay Attack

Mensagens com **timestamp superior a 15 minutos** são automaticamente rejeitadas com `401 Unauthorized`, impedindo que um atacante que interceptou uma mensagem legítima a reenvie posteriormente.

> ⚠️ **Limitação atual:** a API ainda não implementa validação por **nonce** via Redis. Isso significa que a mesma mensagem pode ser reenviada múltiplas vezes dentro da janela de 15 minutos sem ser bloqueada. A integração com Redis para controle de unicidade de mensagens está prevista como próxima melhoria.

---

## 🚀 Endpoints

### `GET /message`

Gera uma mensagem autenticada com os dados climáticos informados.

**Request body:**
```json
{
  "temperature": "25.3",
  "humidity": "68.0",
  "pressure": "1013.2"
}
```

**Response `200 OK`:**
```json
{
  "message": {
    "temperature": "25.3",
    "humidity": "68.0",
    "pressure": "1013.2"
  },
  "timestamp": 1778738994371,
  "hash": "05544686019d9d6b10f530539e1e03d167ccc6311f4ffadac6565d8f4ee7446f..."
}
```

---

### `POST /message`

Valida uma mensagem autenticada recebida pelo servidor.

**Request body:**
```json
{
  "message": {
    "temperature": "25.3",
    "humidity": "68.0",
    "pressure": "1013.2"
  },
  "timestamp": 1778738994371,
  "hash": "05544686019d9d6b10f530539e1e03d167ccc6311f4ffadac6565d8f4ee7446f..."
}
```

**Respostas possíveis:**

| Código | Corpo | Significado |
|--------|-------|-------------|
| `200 OK` | `true` | Mensagem autêntica e íntegra |
| `401 Unauthorized` | — | HMAC inválido ou mensagem expirada (> 15 min) |

---

## 🐳 Executando com Docker

```bash
# criar rede e redis
docker network create minha-rede
docker run -d --name redis --network minha-rede redis:latest redis-server --requirepass zEAM39OcRcs1EI8xAY3UpF2mYQp41Gbj49mAYJL6mLcgRhc6G5

#build e docker
./gradlew build
docker build -t minha-api .
docker run --network minha-rede -p 8080:8080 minha-api
```

A API ficará disponível em: `http://localhost:8080`

---

## 🛠️ Tecnologias

| Tecnologia | Uso |
|---|---|
| **Java 25** | Linguagem principal |
| **Spring Boot** | Framework web e injeção de dependências |
| **HMAC-SHA256** | Algoritmo de autenticação de mensagens |
| **Docker / Docker Compose** | Containerização e execução |



## 📚 Contexto Acadêmico

> **Instituição:** Instituto Federal do Paraná (IFPR)
> **Curso:** Tecnologia em Análise e Desenvolvimento de Sistemas (TADS)
> **Disciplina:** Segurança e Auditoria de Sistemas
> **Professor:** Thiago Berticelli Ló

---

<div align="center">

Feito por **[@fabiomoraisdevbr](https://github.com/fabiomoraisdevbr)** · IFPR Cascavel

</div>

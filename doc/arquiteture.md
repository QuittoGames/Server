# Estrutura do Banco de Dados

## Tabela: User

| Campo | Tipo/Info | Chave |
| :--- | :--- | :--- |
| id | int | PK |
| name | str |  |
| password | str |  |
| email | str |  |
| role | Role (toString) |  |
| linux_uid | int | FK |

## Tabela: LinuxUser

| Campo | Tipo/Info | Chave |
| :--- | :--- | :--- |
| udi | int | PK |
| username | str |  |
| shell | str |  |
| homeDir | str |  |
| groups | int | FK |

## Tabela: Groups

| Campo | Tipo/Info | Chave |
| :--- | :--- | :--- |
| GID | int (unique) | PK |
| name | str |  |
| is_active | bool |  |

## Tabela: Auth / Account (Identificada por campos)

| Campo | Tipo/Info | Chave |
| :--- | :--- | :--- |
| provider_user_id | - | PK |
| user_id | int | FK |
| Provaider | str |  |
| external_client_id | - |  |
| accessToken | str |  |
| refreshToken | str |  |


## Machines
PK Id: int

-- identidade
tailscale_node_key TEXT UNIQUE,

-- rede
hostname TEXT,
current_ip TEXT,

-- wake-on-lan
mac_address TEXT,
wol_enabled BOOLEAN,

-- estado
last_seen TIMESTAMP,
status TEXT
